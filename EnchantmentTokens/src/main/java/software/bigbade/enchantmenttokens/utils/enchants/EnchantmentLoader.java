package software.bigbade.enchantmenttokens.utils.enchants;

import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.EnchantmentAddon;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.utils.EnchantLogger;
import software.bigbade.enchantmenttokens.utils.ReflectionManager;
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
                if (enchants.getName().endsWith(".jar"))
                    loadJar(enchants);
            }

            main.getListenerHandler().loadAddons(addons);
            main.getListenerHandler().loadEnchantments(enchantments);
        });
    }

    public List<Class<?>> loadClasses(File file) throws IOException {
        List<Class<?>> classes = new ArrayList<>();
        JarFile jarFile = new JarFile(file.getAbsolutePath());
        Enumeration<JarEntry> enumerator = jarFile.entries();
        while (enumerator.hasMoreElements()) {
            JarEntry jar = enumerator.nextElement();
            if (jar.isDirectory() || !jar.getName().endsWith(".class")) {
                continue;
            }

            Class<?> loaded = loadClass(jar, file);
            if (loaded == null) continue;
            classes.add(loaded);
        }
        jarFile.close();
        return classes;
    }

    private void loadJar(File file) {
        try {
            EnchantmentAddon addon = null;
            List<Class<?>> classes = loadClasses(file);
            Set<Class<EnchantmentBase>> enchantClasses = new HashSet<>();

            for (Class<?> clazz : classes)
                if (clazz.isAssignableFrom(EnchantmentBase.class))
                    enchantClasses.add((Class<EnchantmentBase>) clazz);
                else if (clazz.isAssignableFrom(EnchantmentAddon.class)) {
                    addon = (EnchantmentAddon) clazz.newInstance();
                    addons.add(addon);
                }
            if (addon == null) {
                EnchantLogger.log(Level.SEVERE, "Jar " + file.getName() + " has no EnchantmentAddon class, skipping loading enchants");
            } else
                enchantments.put(addon.getName(), enchantClasses);
        } catch (IOException | InstantiationException | IllegalAccessException e) {
            EnchantLogger.log(Level.SEVERE, "Could not read jar file", e);
        }
    }

    private Class<?> loadClass(JarEntry jar, File file) {
        try {
            URL[] urls = {new URL("jar:file:" + file.getAbsolutePath() + "!/")};
            URLClassLoader cl = new URLClassLoader(urls, getClass().getClassLoader());
            String className = jar.getName().substring(0, jar.getName().length() - 6);
            className = className.replace('/', '.');
            return cl.loadClass(className);
        } catch (IOException | ClassNotFoundException e) {
            EnchantLogger.log("Problem loading classes", e);
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
