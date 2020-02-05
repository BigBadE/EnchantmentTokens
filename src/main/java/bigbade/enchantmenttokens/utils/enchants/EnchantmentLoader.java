package bigbade.enchantmenttokens.utils.enchants;

import bigbade.enchantmenttokens.EnchantmentTokens;
import bigbade.enchantmenttokens.api.EnchantmentAddon;
import bigbade.enchantmenttokens.api.EnchantmentBase;
import bigbade.enchantmenttokens.utils.EnchantLogger;
import bigbade.enchantmenttokens.utils.ReflectionManager;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

/*
EnchantmentTokens
Copyright (C) 2019-2020 Big_Bad_E
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

public class EnchantmentLoader {
    private Map<String, Set<Class<EnchantmentBase>>> enchantments = new ConcurrentHashMap<>();
    private Collection<EnchantmentAddon> addons = new ConcurrentLinkedQueue<>();

    public EnchantmentLoader(File folder, EnchantmentTokens main) {
        ReflectionManager.setValue(ReflectionManager.getField(Enchantment.class, "acceptingNew"), true, Enchantment.class);
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            if (folder.listFiles() == null) {
                return;
            }
            for (File enchants : Objects.requireNonNull(folder.listFiles())) {
                if (!enchants.getName().endsWith(".jar"))
                    continue;
                loadJar(enchants);
            }

            main.getListenerHandler().loadAddons(addons);
            main.getListenerHandler().loadEnchantments(enchantments);
        });
    }

    private void loadJar(File file) {
        try {
            JarFile jarFile = new JarFile(file.getAbsolutePath());
            Enumeration<JarEntry> enumerator = jarFile.entries();

            EnchantmentAddon addon = null;
            Set<Class<EnchantmentBase>> enchantClasses = new HashSet<>();
            while (enumerator.hasMoreElements()) {
                JarEntry jar = enumerator.nextElement();
                if (jar.isDirectory() || !jar.getName().endsWith(".class")) {
                    continue;
                }

                Object loaded = loadClass(jar, file);
                if(loaded == null) continue;
                if(loaded instanceof Class)
                    enchantClasses.add((Class<EnchantmentBase>) loaded);
                else {
                    addon = (EnchantmentAddon) loaded;
                    addons.add(addon);
                }
            }
            if (addon == null) {
                EnchantLogger.LOGGER.log(Level.SEVERE, "Jar " + file.getName() + " has no EnchantmentAddon class, skipping loading enchants");
            } else
                enchantments.put(addon.getName(), enchantClasses);
        } catch (IOException e) {
            EnchantLogger.LOGGER.log(Level.SEVERE, "Could not load jar at path: " + file.getPath(), e);
        }
    }

    private Object loadClass(JarEntry jar, File file) {
        try {
            URL[] urls = {new URL("jar:file:" + file.getAbsolutePath() + "!/")};
            URLClassLoader cl = new URLClassLoader(urls, getClass().getClassLoader());
            String className = jar.getName().substring(0, jar.getName().length() - 6);
            className = className.replace('/', '.');
            Class<?> clazz = cl.loadClass(className);
            if (EnchantmentBase.class.equals(clazz.getSuperclass())) {
                return clazz;
            } else if (EnchantmentAddon.class.equals(clazz.getSuperclass())) {
                return clazz.newInstance();
            }
        } catch (IOException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Collection<EnchantmentAddon> getAddons() {
        return addons;
    }

    public Map<String, Set<Class<EnchantmentBase>>> getEnchantments() {
        return enchantments;
    }
}
