/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.utils.enchants;

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
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

public class EnchantmentFileLoader {
    private final Map<EnchantmentAddon, Set<Class<EnchantmentBase>>> enchantments = new ConcurrentHashMap<>();
    private final Collection<EnchantmentAddon> addons = new ConcurrentLinkedQueue<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public EnchantmentFileLoader(File folder, EnchantmentTokens main) {
        main.getScheduler().runTaskAsync(() -> {
            if (folder.listFiles() == null) {
                return;
            }
            List<File> jars = new ArrayList<>();
            for (File enchants : Objects.requireNonNull(folder.listFiles())) {
                if (enchants.getName().endsWith(".jar"))
                    jars.add(enchants);
            }

            loadEnchantmentClasses(jars, main);
        });
    }

    public static <T> Set<Class<T>> loadClasses(File file, Class<T> clazz) {
        Set<Class<T>> classes = new HashSet<>();
        URL[] urls = getUrls(Collections.singletonList(file));
        try (URLClassLoader cl = new URLClassLoader(urls, EnchantmentFileLoader.class.getClassLoader())) {
            classes = loadClassesForFile(file, cl, clazz);
        } catch (IOException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not load storage jar", e);
        }
        return classes;
    }

    @SuppressWarnings("unchecked")
    private static <T> Set<Class<T>> loadClassesForFile(File file, ClassLoader cl, Class<? extends T> clazz) {
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

    @SneakyThrows
    private static URL[] getUrls(List<File> files) {
        URL[] urls = new URL[files.size()];
        for (int i = 0; i < files.size(); i++) {
            urls[i] = new URL("jar:file:" + files.get(i).getAbsolutePath() + "!/");
        }
        return urls;
    }

    @Nullable
    private static EnchantmentAddon loadAddon(File file, EnchantmentTokens main, ClassLoader cl) {
        try (JarFile jarFile = new JarFile(file.getAbsolutePath()); InputStream stream = jarFile.getInputStream(jarFile.getEntry("addon.yml"))) {
            PluginDescriptionFile descriptionFile = new PluginDescriptionFile(stream);
            EnchantmentAddon addon = (EnchantmentAddon) ReflectionManager.instantiate(cl.loadClass(descriptionFile.getMain()));
            addon.setup(main, descriptionFile);
            return addon;
        } catch (IOException | InvalidDescriptionException | ClassNotFoundException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, e, () -> "Problem loading addon " + file.getName());
        }
        return null;
    }

    private static Class<?> loadClass(JarEntry jar, ClassLoader loader) throws ClassNotFoundException {
        String className = jar.getName().substring(0, jar.getName().length() - 6);
        className = className.replace('/', '.');
        return loader.loadClass(className);
    }

    public void loadEnchantmentClasses(List<File> files, EnchantmentTokens main) {
        URL[] urls = getUrls(files);
        try (URLClassLoader cl = new URLClassLoader(urls, EnchantmentFileLoader.class.getClassLoader())) {
            for (File file : files) {
                loadJar(file, main, cl);
            }
            for (EnchantmentAddon addon : addons) {
                executor.submit(() -> main.getEnchantmentLoader().loadAddon(addon));
            }
            executor.shutdown();
            main.getEnchantmentLoader().loadEnchantments(enchantments);
        } catch (IOException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Problem loading jar URLs", e);
        }
    }

    private void loadJar(File file, EnchantmentTokens main, ClassLoader cl) {
        Set<Class<EnchantmentBase>> classes = loadClassesForFile(file, cl, EnchantmentBase.class);

        EnchantmentAddon addon = loadAddon(file, main, cl);
        if (addon != null) {
            addons.add(addon);
            enchantments.put(addon, classes);
        } else {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Jar {0} has a bugged/has no EnchantmentAddon class, skipping loading enchants", file.getName());
        }
    }

    public Collection<EnchantmentAddon> getAddons() {
        return addons;
    }
}
