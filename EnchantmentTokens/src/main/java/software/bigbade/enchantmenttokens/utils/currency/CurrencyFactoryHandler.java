package software.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.ExternalCurrencyData;
import software.bigbade.enchantmenttokens.utils.configuration.ConfigurationManager;
import software.bigbade.enchantmenttokens.utils.EnchantLogger;
import software.bigbade.enchantmenttokens.utils.configuration.ConfigurationType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarFile;
import java.util.logging.Level;

public class CurrencyFactoryHandler {
    private EnchantmentTokens main;
    private ConfigurationSection section;
    private int version;

    public CurrencyFactoryHandler(EnchantmentTokens main, ConfigurationSection section, int version) {
        this.main = main;
        this.section = section;
        this.version = version;
    }

    public CurrencyFactory load() {
        String type = new ConfigurationType<String>("gems").getValue("type", section);

        if ("gems".equalsIgnoreCase(type)) {
            return loadGemFactory();
        } else if ("vault".equalsIgnoreCase(type)) {
            return new VaultCurrencyFactory(main.getServer());
        } else {
            CurrencyFactory factory = loadExternalJar(main, section);
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
            boolean persistent = new ConfigurationType<Boolean>(true).getValue("usePersistentData", section);
            if (persistent)
                return new LatestCurrencyFactory(new NamespacedKey(main, "gems"));
            else
                return new GemCurrencyFactory(main);
        } else
            return new GemCurrencyFactory(main);
    }

    private CurrencyFactory loadExternalJar(EnchantmentTokens main, ConfigurationSection section) {
        File found = ConfigurationManager.getFolder(main.getDataFolder().getAbsolutePath() + "\\storage");
        if (found.listFiles() == null)
            return null;
        for (File subfile : Objects.requireNonNull(found.listFiles())) {
            if (!subfile.getName().endsWith(".jar"))
                continue;
            CurrencyFactory factory = loadFactory(main, section, subfile);
            if (factory != null)
                return factory;
        }
        return null;
    }

    private CurrencyFactory loadFactory(EnchantmentTokens main, ConfigurationSection section, File file) {
        try (JarFile jarFile = new JarFile(file.getAbsolutePath())) {
            InputStream stream = jarFile.getInputStream(jarFile.getEntry("config.yml"));
            FileConfiguration configuration = new YamlConfiguration();
            configuration.load(new InputStreamReader(stream, Charset.defaultCharset()));
            stream.close();
            ExternalCurrencyData data = new ExternalCurrencyData(configuration);
            if (!data.matches(section.getString("type"))) {
                return null;
            }
            List<Class<?>> classes = main.getLoader().loadClasses(file);
            for (Class<?> clazz : classes) {
                if (clazz.getInterfaces()[0].equals(CurrencyFactory.class)) {
                    return (CurrencyFactory) clazz.getConstructors()[0].newInstance(section);
                }
            }
        } catch (IOException | InvalidConfigurationException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            EnchantLogger.log("Could not load currency handler (is it valid?)", e);
        }
        return null;
    }
}
