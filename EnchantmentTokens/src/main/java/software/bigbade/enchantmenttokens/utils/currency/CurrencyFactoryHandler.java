package software.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import software.bigbade.enchantmenttokens.utils.EnchantLogger;
import software.bigbade.enchantmenttokens.utils.FileHelper;
import software.bigbade.enchantmenttokens.utils.ReflectionManager;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;
import software.bigbade.enchantmenttokens.utils.configuration.ConfigurationManager;
import software.bigbade.enchantmenttokens.utils.configuration.ConfigurationType;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarFile;
import java.util.logging.Level;

public class CurrencyFactoryHandler {
    private ConfigurationSection section;
    private int version;
    private String filePath;
    private SchedulerHandler scheduler;

    public CurrencyFactoryHandler(String filePath, SchedulerHandler scheduler, ConfigurationSection section, int version) {
        this.filePath = filePath;
        this.scheduler = scheduler;
        this.section = section;
        this.version = version;
    }

    public CurrencyFactory load() {
        String type = new ConfigurationType<>("gems").getValue("type", section);

        if ("gems".equalsIgnoreCase(type)) {
            return loadGemFactory();
        } else if ("vault".equalsIgnoreCase(type)) {
            return new VaultCurrencyFactory(Bukkit.getServer());
        } else {
            CurrencyFactory factory = loadExternalJar(type);
            if (factory != null && factory.loaded())
                return factory;
            else {
                if (factory == null) {
                    EnchantLogger.log(Level.SEVERE, "Could not find type {0}, defaulted to gems", type);
                    section.set("type", "gems");
                }
                EnchantLogger.log(Level.SEVERE, "Could not load currency factory");
                return loadGemFactory();
            }
        }
    }

    private CurrencyFactory loadGemFactory() {
        if (version >= 14) {
            boolean persistent = new ConfigurationType<>(true).getValue("usePersistentData", section);
            if (persistent)
                return new LatestCurrencyFactory();
            else
                return new GemCurrencyFactory(scheduler, filePath);
        } else
            return new GemCurrencyFactory(scheduler, filePath);
    }

    private CurrencyFactory loadExternalJar(String type) {
        File found = ConfigurationManager.getFolder(filePath + "\\storage");
        if (found.listFiles() == null)
            return null;
        for (File subfile : Objects.requireNonNull(found.listFiles())) {
            if (!subfile.getName().endsWith(".jar"))
                continue;
            CurrencyFactory factory = loadFactory(type, subfile);
            if (factory != null)
                return factory;
        }
        return null;
    }

    private CurrencyFactory loadFactory(String type, File file) {
        try (JarFile jarFile = FileHelper.getJarFile(file.getAbsolutePath());
             InputStream stream = FileHelper.getJarStream(jarFile, "config.yml")) {
            if(stream == null)
                EnchantLogger.log(Level.SEVERE, "Invalid currency handler at {0}", file.getAbsolutePath());
            FileConfiguration configuration = ConfigurationManager.loadConfigurationStream(stream);

            if (!new ConfigurationType<>("error").getValue("name", configuration).equals(type))
                return null;

            List<Class<?>> classes = EnchantmentLoader.loadClasses(file);
            return getFactory(classes);
        } catch (IOException e) {
            EnchantLogger.log("Could not open jar", e);
        }
        return null;
    }

    private CurrencyFactory getFactory(List<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            if (clazz.getInterfaces()[0].equals(CurrencyFactory.class))
                return (CurrencyFactory) ReflectionManager.instantiate(clazz.getConstructors()[0], section);
        }
        return null;
    }
}
