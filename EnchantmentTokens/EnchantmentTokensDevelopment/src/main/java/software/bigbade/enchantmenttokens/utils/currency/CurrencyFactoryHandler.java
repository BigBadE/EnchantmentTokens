package software.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.configuration.ConfigurationManager;
import software.bigbade.enchantmenttokens.configuration.ConfigurationType;
import software.bigbade.enchantmenttokens.currency.CurrencyFactory;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarFile;
import java.util.logging.Level;

public class CurrencyFactoryHandler {
    private EnchantmentTokens main;
    private ConfigurationSection section;

    public CurrencyFactoryHandler(EnchantmentTokens main) {
        this.main = main;
        section = ConfigurationManager.getSectionOrCreate(main.getConfig(), "currency");
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
                    EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not find type {0}, defaulted to gems", type);
                    section.set("type", "gems");
                }
                EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not load currency factory");
                return loadGemFactory();
            }
        }
    }

    private CurrencyFactory loadGemFactory() {
        if (main.getVersion() >= 14) {
            boolean persistent = new ConfigurationType<>(true).getValue("usePersistentData", section);
            if (persistent)
                return new LatestCurrencyFactory(new NamespacedKey(main, "gems"));
            else
                return new GemCurrencyFactory(main.getScheduler(), main.getDataFolder().getAbsolutePath());
        } else
            return new GemCurrencyFactory(main.getScheduler(), main.getDataFolder().getAbsolutePath());
    }

    private CurrencyFactory loadExternalJar(String type) {
        File found = ConfigurationManager.getFolder(main.getDataFolder().getAbsolutePath() + "\\storage");
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
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not load currency handler (is it valid?)", e);
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
