package software.bigbade.enchantmenttokens.utils.enchants;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.Plugin;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.CustomEnchantment;
import software.bigbade.enchantmenttokens.api.EnchantmentAddon;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.utils.ReflectionManager;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
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
    private Collection<Plugin> addons = new ConcurrentLinkedQueue<>();

    public EnchantmentLoader(File folder, EnchantmentTokens main) {
        ReflectionManager.setValue(ReflectionManager.getField(Enchantment.class, "acceptingNew"), true, Enchantment.class);
        main.getScheduler().runTaskAsync(() -> {
            if (folder.listFiles() == null) {
                return;
            }
            for (File enchants : Objects.requireNonNull(folder.listFiles())) {
                if (enchants.getName().endsWith(".jar"))
                    loadJar(enchants, main);
            }

            main.getListenerHandler().loadAddons(addons);
            main.getListenerHandler().loadEnchantments(enchantments);
        });
    }

    public static List<Class<?>> loadClasses(File file) {
        List<Class<?>> classes = new ArrayList<>();
        try (JarFile jarFile = new JarFile(file.getAbsolutePath())) {
            Enumeration<JarEntry> enumerator = jarFile.entries();
            while (enumerator.hasMoreElements()) {
                JarEntry jar = enumerator.nextElement();
                if (jar.isDirectory() || !jar.getName().endsWith(".class")) {
                    continue;
                }

                Class<?> loaded = loadClass(jar, file);
                if (loaded != null)
                    classes.add(loaded);
            }
        } catch (IOException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not load external jar file", e);
        }
        return classes;
    }

    @SuppressWarnings("unchecked")
    private void loadJar(File file, EnchantmentTokens main) {
        EnchantmentAddon addon = null;
        List<Class<?>> classes = loadClasses(file);
        Set<Class<EnchantmentBase>> enchantClasses = new HashSet<>();

        for (Class<?> clazz : classes)
            if (CustomEnchantment.class.isAssignableFrom(clazz))
                enchantClasses.add((Class<EnchantmentBase>) clazz);
            else if (EnchantmentAddon.class.isAssignableFrom(clazz)) {
                addon = loadAddon(file, (Class<EnchantmentAddon>) clazz, main);
                if (addon != null)
                    addons.add(addon);
            }
        if (addon == null) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Jar " + file.getName() + " has a bugged/no EnchantmentAddon class, skipping loading enchants");
        } else {
            enchantments.put(addon, enchantClasses);
        }
    }

    @Nullable
    private EnchantmentAddon loadAddon(File file, Class<EnchantmentAddon> clazz, EnchantmentTokens main) {
        try (JarFile jarFile = new JarFile(file.getAbsolutePath()); InputStream stream = jarFile.getInputStream(jarFile.getEntry("addon.yml"))) {
            EnchantmentAddon addon = clazz.getDeclaredConstructor().newInstance();
            addon.setup(main, stream);
            return addon;
        } catch (IOException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException | InvalidDescriptionException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Problem loading addon " + file.getName() + " class " + clazz.getSimpleName(), e);
        }
        return null;
    }

    private static Class<?> loadClass(JarEntry jar, File file) throws MalformedURLException {
        URL[] urls = {new URL("jar:file:" + file.getAbsolutePath() + "!/")};
        try (URLClassLoader cl = new URLClassLoader(urls, EnchantmentLoader.class.getClassLoader())) {
            String className = jar.getName().substring(0, jar.getName().length() - 6);
            className = className.replace('/', '.');
            return cl.loadClass(className);
        } catch (IOException | ClassNotFoundException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Problem loading classes", e);
        }
        return null;
    }

    public Collection<Plugin> getAddons() {
        return addons;
    }
}
