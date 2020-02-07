package software.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.EnchantmentAddon;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.ExternalCurrencyData;
import software.bigbade.enchantmenttokens.utils.ConfigurationManager;
import software.bigbade.enchantmenttokens.utils.EnchantLogger;
import sun.rmi.runtime.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

public class CurrencyFactoryHandler {

    public CurrencyFactory load(EnchantmentTokens main, ConfigurationSection section) {
        File found = ConfigurationManager.getFolder(main.getDataFolder().getAbsolutePath() + "\\");
        if (found.listFiles() != null)
            for (File subfile : found.listFiles()) {
                if (!subfile.getName().endsWith(".jar"))
                    continue;
                try {
                    CurrencyFactory factory = null;
                    JarFile jarFile = new JarFile(subfile.getAbsolutePath());
                    InputStream stream = jarFile.getInputStream(jarFile.getEntry("config.yml"));
                    FileConfiguration configuration = new YamlConfiguration();
                    configuration.load(new InputStreamReader(stream));
                    stream.close();
                    ExternalCurrencyData data = new ExternalCurrencyData(configuration);
                    if(!data.matches(section.getString("type"))) {
                        jarFile.close();
                        stream.close();
                        continue;
                    }
                    Enumeration<JarEntry> enumerator = jarFile.entries();
                    while (enumerator.hasMoreElements()) {
                        JarEntry jar = enumerator.nextElement();
                        if (jar.isDirectory() || !jar.getName().endsWith(".class")) {
                            continue;
                        }
                        Class foundClass = (Class) loadClass(jar, subfile);
                        if(foundClass.isAssignableFrom(CurrencyFactory.class)) {
                            try {
                                factory = (CurrencyFactory) foundClass.getConstructor(EnchantmentTokens.class, ConfigurationSection.class).newInstance(main, section);
                            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                                EnchantLogger.LOGGER.log(Level.SEVERE, "Error loading currency factory", e);
                            }
                        }
                    }
                    jarFile.close();
                    return factory;
                } catch (IOException | InvalidConfigurationException e) {
                    EnchantLogger.LOGGER.log(Level.SEVERE, "Could not load currency handler (is it valid?)", e);
                }
            }
        return null;
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
}
