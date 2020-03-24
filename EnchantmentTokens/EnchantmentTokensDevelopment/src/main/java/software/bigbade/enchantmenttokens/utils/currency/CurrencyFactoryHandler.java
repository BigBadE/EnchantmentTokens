package software.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import software.bigbade.enchantmenttokens.currency.CurrencyFactory;
import software.bigbade.enchantmenttokens.utils.EnchantLogger;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;
import software.bigbade.enchantmenttokens.configuration.ConfigurationManager;
import software.bigbade.enchantmenttokens.configuration.ConfigurationType;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentLoader;
import software.bigbade.enchantmenttokens.utils.enchants.FakePlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
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
                return new LatestCurrencyFactory(new NamespacedKey(FakePlugin.ENCHANTMENTPLUGIN, "gems"));
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
        try (JarFile jarFile = new JarFile(file.getAbsolutePath()); InputStream stream = jarFile.getInputStream(jarFile.getEntry("config.yml"))) {
            FileConfiguration configuration = ConfigurationManager.loadConfigurationStream(stream);

            if (!new ConfigurationType<>("gems").getValue("name", configuration).equals(type))
                return null;

            List<Class<?>> classes = EnchantmentLoader.loadClasses(file);
            return getFactory(classes);
        } catch (IOException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            EnchantLogger.log("Could not load currency handler (is it valid?)", e);
        }
        return null;
    }

    private CurrencyFactory getFactory(List<Class<?>> classes) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        for (Class<?> clazz : classes) {
            if (clazz.getInterfaces()[0].equals(CurrencyFactory.class))
                return (CurrencyFactory) clazz.getConstructors()[0].newInstance(section);
        }
        return null;
    }
}
