/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.utils.enchants;

import co.aikar.taskchain.TaskChain;
import lombok.SneakyThrows;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginDescriptionFile;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.EnchantmentAddon;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.utils.ReflectionManager;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

public class EnchantmentFileLoader {
    private final Collection<EnchantmentAddon> addons = new ConcurrentLinkedQueue<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final EnchantmentTokens main;

    public EnchantmentFileLoader(File folder, EnchantmentTokens main) {
        this.main = main;
        TaskChain<?> chain = EnchantmentTokens.newChain();
        chain.async(() -> {
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
        });
        chain.execute();
    }

    private void loadEnchantmentClasses(List<File> jars) {
        long start = System.currentTimeMillis();
        URL[] urls = getUrls(jars);
        Set<Future<?>> futures = new HashSet<>();
        for (File jar : jars) {
            futures.add(executor.submit(() -> {
                try (URLClassLoader loader = new URLClassLoader(urls, getClass().getClassLoader())) {
                    Thread.currentThread().setContextClassLoader(loader);
                    Set<Class<EnchantmentBase>> classes = loadClassesForFile(jar, loader, EnchantmentBase.class);
                    EnchantmentAddon addon = loadAddon(jar, loader);
                    if (addon != null) {
                        addons.add(addon);
                        main.getEnchantmentLoader().loadEnchantments(addon, main.getEnchantmentHandler(), classes);
                    }
                } catch (IOException e) {
                    EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Error loading enchantments", e);
                }
            }));
        }
        executor.submit(() -> {
            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                    EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Error waiting for enchants to load", e);
                }
            }
            main.getListenerHandler().registerListeners();
            main.saveConfig();
            EnchantmentTokens.getEnchantLogger().log(Level.INFO, "Loaded enchantments in {0}ms", System.currentTimeMillis() - start);
            executor.shutdown();
        });
    }

    @Nullable
    private EnchantmentAddon loadAddon(File file, ClassLoader cl) {
        try (JarFile jarFile = new JarFile(file.getAbsolutePath()); InputStream stream = jarFile.getInputStream(jarFile.getEntry("addon.yml"))) {
            if (stream == null) {
                throw new InvalidDescriptionException("Found no addon file for " + file.getName());
            }
            PluginDescriptionFile descriptionFile = new PluginDescriptionFile(stream);
            Class<?> addonClass = cl.loadClass(descriptionFile.getMain());
            if (addonClass == null || !EnchantmentAddon.class.isAssignableFrom(addonClass)) {
                throw new ClassNotFoundException("No/Invalid EnchantmentAddon class.");
            }
            EnchantmentAddon addon = (EnchantmentAddon) ReflectionManager.instantiate(addonClass);
            addon.setup(main, descriptionFile);
            main.getEnchantmentLoader().loadAddon(addon);
            return addon;
        } catch (IOException | InvalidDescriptionException | ClassNotFoundException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, e, () -> "Problem loading addon " + file.getName());
        }
        return null;
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

    public Collection<EnchantmentAddon> getAddons() {
        return addons;
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