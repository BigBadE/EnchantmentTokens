package software.bigbade.enchantmenttokens.utils.currency;

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
import java.util.List;
import java.util.Objects;
import java.util.jar.JarFile;
import java.util.logging.Level;

public class CurrencyFactoryHandler {

    public CurrencyFactory load(EnchantmentTokens main, ConfigurationSection section, int version) {
        String type = new ConfigurationType<String>("gems").getValue("type", section);

        if ("gems".equalsIgnoreCase(type)) {
            if (version >= 14) {
                boolean persistent = new ConfigurationType<Boolean>(true).getValue("usePersistentData", section);
                if (persistent)
                    return new LatestCurrencyFactory(main);
                else
                    return new GemCurrencyFactory(main);
            } else
                return new GemCurrencyFactory(main);
        } else if("vault".equalsIgnoreCase(type)) {
            return new VaultCurrencyFactory(main.getServer());
        } else {
            return loadExternalJar(main, section);
        }
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
        try {
            JarFile jarFile = new JarFile(file.getAbsolutePath());
            InputStream stream = jarFile.getInputStream(jarFile.getEntry("config.yml"));
            FileConfiguration configuration = new YamlConfiguration();
            configuration.load(new InputStreamReader(stream));
            stream.close();
            ExternalCurrencyData data = new ExternalCurrencyData(configuration);
            if (!data.matches(section.getString("type"))) {
                jarFile.close();
                return null;
            }
            List<Class<?>> classes = main.getLoader().loadClasses(file);
            for (Class<?> clazz : classes) {
                if (clazz.getInterfaces()[0].equals(CurrencyFactory.class)) {
                    return (CurrencyFactory) clazz.getConstructors()[0].newInstance(main, section);
                }
            }
        } catch (IOException | InvalidConfigurationException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            EnchantLogger.log(Level.SEVERE, "Could not load currency handler (is it valid?)", e);
        }
        return null;
    }
}
