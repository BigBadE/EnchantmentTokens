package bigbade.enchantmenttokens.api;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnchantmentLoader {
    private Set<Class> enchantments = new HashSet<>();
    private Set<EnchantmentAddon> addons = new HashSet<>();

    public EnchantmentLoader(File folder, Logger logger) {
        try {
            for (File enchants : folder.listFiles()) {
                if (enchants.getName().endsWith(".jar")) {
                    try {
                        JarFile jarFile = new JarFile(enchants.getAbsolutePath());
                        Enumeration<JarEntry> enumerator = jarFile.entries();

                        URL[] urls = {new URL("jar:file:" + enchants.getAbsolutePath() + "!/")};
                        URLClassLoader cl = new URLClassLoader(urls, getClass().getClassLoader());
                        while (enumerator.hasMoreElements()) {
                            JarEntry file = enumerator.nextElement();
                            if (file.isDirectory() || !file.getName().endsWith(".class")) {
                                continue;
                            }

                            String className = file.getName().substring(0, file.getName().length() - 6);
                            className = className.replace('/', '.');
                            Class clazz = cl.loadClass(className);
                            if (clazz.getSuperclass().equals(EnchantmentBase.class)) {
                                enchantments.add(clazz);
                            } else if(clazz.getSuperclass().equals(EnchantmentAddon.class)) {
                               addons.add((EnchantmentAddon) clazz.newInstance());
                            }
                        }
                    } catch (Exception e) {
                        logger.severe("Could not load jar at path: " + enchants.getPath());
                        logger.log(Level.SEVERE, e, () -> "Error:");
                    }
                }
            }
        } catch(NullPointerException ignored) {

        }
    }

    public Set<EnchantmentAddon> getAddons() {
        return addons;
    }

    public Set<Class> getEnchantments() {
        return enchantments;
    }
}
