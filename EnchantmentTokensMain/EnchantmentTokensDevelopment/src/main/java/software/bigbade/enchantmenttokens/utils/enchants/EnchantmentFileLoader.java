/*
 * Addons for the Custom Enchantment API in Minecraft
 * Copyright (C) 2020 BigBadE
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

public class EnchantmentFileLoader {
    private final EnchantmentTokens main;
    private final File folder;

    @Getter
    private final List<EnchantmentAddon> addons = new ArrayList<>();

    public EnchantmentFileLoader(File folder, EnchantmentTokens main) {
        EnchantmentTokens.getEnchantLogger().log(Level.INFO, "Looking for enchantments");
        this.main = main;
        this.folder = folder;
        new EnchantmentChain<>().async(this::loadJars).execute();
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
        EnchantmentChain chain = new EnchantmentChain<>().split();
        for (int i = 0; i < jars.size(); i++) {
            File jar = jars.get(i);
            URL url = urls[i];
            chain.async(() -> loadJarFile(jar, url));
        }
        chain.collect().async(() -> finishLoading(start)).execute();
    }

    private void loadJarFile(File jar, URL url) {
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
    }

    private void finishLoading(long start) {
        LocaleManager.updateLocale(main.getConfig(), addons);
        EnchantmentTokens.getEnchantLogger().log(Level.INFO, "Loaded enchantments in {0}ms", System.currentTimeMillis() - start);
        main.getListenerHandler().registerListeners();
        //main.saveConfig();
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
}