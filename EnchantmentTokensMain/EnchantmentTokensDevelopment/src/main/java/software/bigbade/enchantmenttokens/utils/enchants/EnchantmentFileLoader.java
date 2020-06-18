/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.utils.enchants;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginDescriptionFile;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.EnchantmentAddon;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.wrappers.EnchantmentChain;
import software.bigbade.enchantmenttokens.exceptions.AddonLoadingException;
import software.bigbade.enchantmenttokens.localization.LocaleManager;
import software.bigbade.enchantmenttokens.logging.UnwrappedErrorLogger;
import software.bigbade.enchantmenttokens.utils.ReflectionManager;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

public class EnchantmentFileLoader {
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final EnchantmentTokens main;
    private final File folder;

    @Getter
    private final List<EnchantmentAddon> addons = new ArrayList<>();

    public EnchantmentFileLoader(File folder, EnchantmentTokens main) {
        EnchantmentTokens.getEnchantLogger().log(Level.INFO, "Looking for enchantments");
        this.main = main;
        this.folder = folder;
        EnchantmentChain chain = new EnchantmentChain();
        chain.async(this::loadJars).execute();
    }

    public void loadJars() {
        if (folder.listFiles() == null) {
            return;
        }
        List<File> jars = new ArrayList<>();
        for (File enchants : Objects.requireNonNull(folder.listFiles())) {
            if (enchants.getName().endsWith(".jar")) {
                jars.add(enchants);
            }
        }
        loadEnchantmentClasses(jars);
    }

    private void loadEnchantmentClasses(List<File> jars) {
        long start = System.currentTimeMillis();
        URL[] urls = getUrls(jars);
        Future<?>[] futures = new Future[jars.size()];

        for (int i = 0; i < jars.size(); i++) {
            File jar = jars.get(i);
            futures[i] = loadJarFile(jar, urls[i]);
        }
        executor.submit(() -> finishLoading(jars, futures, start));
    }

    private Future<?> loadJarFile(File jar, URL url) {
        return executor.submit(() -> {
            try (URLClassLoader loader = new URLClassLoader(new URL[]{url}, getClass().getClassLoader())) {
                Thread.currentThread().setContextClassLoader(loader);
                EnchantmentAddon addon = loadAddon(jar, loader);
                Set<Class<EnchantmentBase>> classes = loadClassesForFile(jar, loader, EnchantmentBase.class);
                if (addon != null) {
                    addons.add(addon);
                    main.getEnchantmentLoader().loadEnchantments(addon, main.getEnchantmentHandler(), classes);
                }
            } catch (IOException e) {
                EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Error loading enchantments", e);
            }
        });
    }

    private void finishLoading(List<File> jars, Future<?>[] futures, long start) {
        for (int i = 0; i < futures.length; i++) {
            try {
                futures[i].get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                EnchantmentAddon addon = null;
                if (addons.size() > i) {
                    addon = addons.get(i);
                }
                if (addon == null) {
                    UnwrappedErrorLogger.logFileError(jars.get(i), e);
                } else {
                    UnwrappedErrorLogger.logAddonError(addon, e);
                }
            }
        }
        LocaleManager.updateLocale(main.getConfig(), addons);
        EnchantmentTokens.getEnchantLogger().log(Level.INFO, "Loaded enchantments in {0}ms", System.currentTimeMillis() - start);
        main.getListenerHandler().registerListeners();
        main.saveConfig();
        Thread.currentThread().setName("Enchantment-Loader");
        executor.shutdown();
    }

    @Nullable
    private EnchantmentAddon loadAddon(File file, ClassLoader cl) {
        try (JarFile jarFile = new JarFile(file.getAbsolutePath()); InputStream stream = jarFile.getInputStream(jarFile.getEntry("addon.yml"))) {
            if (stream == null) {
                throw new InvalidDescriptionException("Found no addon file for " + file.getName());
            }
            PluginDescriptionFile descriptionFile = new PluginDescriptionFile(stream);
            EnchantmentAddon addon = instanceAddonClass(cl, descriptionFile);
            if (addon == null) {
                return null;
            }

            addon.setup(main, descriptionFile);
            main.getEnchantmentLoader().loadAddon(addon);
            return addon;
        } catch (IOException | InvalidDescriptionException e) {
            throw new AddonLoadingException("Problem loading addon " + file.getName(), e);
        }
    }

    @Nullable
    private EnchantmentAddon instanceAddonClass(ClassLoader cl, PluginDescriptionFile descriptionFile) throws InvalidDescriptionException {
        Class<?> addonClass;
        try {
            addonClass = cl.loadClass(descriptionFile.getMain());
        } catch (ClassNotFoundException e) {
            throw new InvalidDescriptionException("Main attribute " + descriptionFile.getMain() + " does not point to a valid addon.");
        }
        if (addonClass == null || !EnchantmentAddon.class.isAssignableFrom(addonClass)) {
            return null;
        }

        return (EnchantmentAddon) ReflectionManager.instantiate(addonClass);
    }

    @SuppressWarnings("unchecked")
    public static <T> Set<Class<T>> loadClassesForFile(File file, ClassLoader cl, Class<T> clazz) {
        Set<Class<T>> enchantments = new HashSet<>();
        try (JarFile jarFile = new JarFile(file.getAbsolutePath())) {
            Enumeration<JarEntry> enumerator = jarFile.entries();
            while (enumerator.hasMoreElements()) {
                JarEntry jar = enumerator.nextElement();
                if (jar.isDirectory() || !jar.getName().endsWith(".class")) {
                    continue;
                }

                Class<?> loaded = loadClass(jar, cl);
                if (loaded != null && clazz.isAssignableFrom(loaded)) {
                    enchantments.add((Class<T>) loaded);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not load external jar file", e);
        }
        return enchantments;
    }

    private static Class<?> loadClass(JarEntry jar, ClassLoader loader) throws ClassNotFoundException {
        String className = jar.getName().substring(0, jar.getName().length() - 6);
        className = className.replace('/', '.');
        return loader.loadClass(className);
    }

    @SneakyThrows
    private static URL[] getUrls(List<File> files) {
        URL[] urls = new URL[files.size()];
        for (int i = 0; i < files.size(); i++) {
            urls[i] = new URL("jar:file:" + files.get(i).getAbsolutePath() + "!/");
        }
        return urls;
    }
}