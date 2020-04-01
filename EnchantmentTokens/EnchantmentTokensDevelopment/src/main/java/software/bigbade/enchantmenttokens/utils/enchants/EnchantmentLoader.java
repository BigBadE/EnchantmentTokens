/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.utils.enchants;

import lombok.SneakyThrows;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginDescriptionFile;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.CustomEnchantment;
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
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

public class EnchantmentLoader {
    private Map<EnchantmentAddon, Set<Class<EnchantmentBase>>> enchantments = new ConcurrentHashMap<>();
    private Collection<EnchantmentAddon> addons = new ConcurrentLinkedQueue<>();

    public EnchantmentLoader(File folder, EnchantmentTokens main) {
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

            main.getListenerHandler().loadAddons(addons);
            main.getListenerHandler().loadEnchantments(enchantments);
        });
    }

    public static List<Class<?>> loadClasses(File file) {
        List<Class<?>> classes = new ArrayList<>();
        URL[] urls = getUrls(Collections.singletonList(file));
        try (URLClassLoader cl = new URLClassLoader(urls, EnchantmentLoader.class.getClassLoader())) {
            classes = loadClassesForFile(file, cl);
        } catch (IOException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not load storage jar", e);
        }
        return classes;
    }

    @SuppressWarnings("unchecked")
    public void loadEnchantmentClasses(List<File> files, EnchantmentTokens main) {
        URL[] urls = getUrls(files);
        try (URLClassLoader cl = new URLClassLoader(urls, EnchantmentLoader.class.getClassLoader())) {
            for (File file : files) {
                List<Class<?>> classes = loadClassesForFile(file, cl);
                Set<Class<EnchantmentBase>> enchantClasses = new HashSet<>();

                for (Class<?> clazz : classes)
                    if (CustomEnchantment.class.isAssignableFrom(clazz))
                        enchantClasses.add((Class<EnchantmentBase>) clazz);
                EnchantmentAddon addon = loadAddon(file, main, cl);
                if (addon != null) {
                    addons.add(addon);
                    enchantments.put(addon, enchantClasses);
                } else {
                    EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Jar " + file.getName() + " has a bugged/no EnchantmentAddon class, skipping loading enchants");
                }
            }
        } catch (IOException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Problem loading jar URLs", e);
        }
    }

    private static List<Class<?>> loadClassesForFile(File file, ClassLoader cl) {
        List<Class<?>> classes = new ArrayList<>();
        try (JarFile jarFile = new JarFile(file.getAbsolutePath())) {
            Enumeration<JarEntry> enumerator = jarFile.entries();
            while (enumerator.hasMoreElements()) {
                JarEntry jar = enumerator.nextElement();
                if (jar.isDirectory() || !jar.getName().endsWith(".class")) {
                    continue;
                }

                Class<?> loaded = loadClass(jar, cl);
                if (loaded != null)
                    classes.add(loaded);
            }
        } catch (IOException | ClassNotFoundException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not load external jar file", e);
        }
        return classes;
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
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Problem loading addon " + file.getName(), e);
        }
        return null;
    }

    private static Class<?> loadClass(JarEntry jar, ClassLoader loader) throws ClassNotFoundException {
        String className = jar.getName().substring(0, jar.getName().length() - 6);
        className = className.replace('/', '.');
        return loader.loadClass(className);
    }

    public Collection<EnchantmentAddon> getAddons() {
        return addons;
    }
}
