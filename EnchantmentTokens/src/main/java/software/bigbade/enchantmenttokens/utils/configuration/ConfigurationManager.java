package software.bigbade.enchantmenttokens.utils.configuration;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.ConfigurationField;
import software.bigbade.enchantmenttokens.utils.EnchantLogger;
import software.bigbade.enchantmenttokens.utils.ReflectionManager;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;
import java.util.logging.Level;

public class ConfigurationManager {
    private ConfigurationManager() { }

    public static FileConfiguration loadConfigurationFile(File config) {
        FileConfiguration configuration = new YamlConfiguration();
        try {
            createFile(config);
            configuration.load(config);
        } catch (IOException | InvalidConfigurationException e) {
            EnchantLogger.log(Level.SEVERE, "could not load enchantment configuration", e);
        }
        return configuration;
    }

    public static void deleteFile(File file) {
        if (file.exists()) {
            try {
                Files.delete(file.toPath());
            } catch (IOException e) {
                EnchantLogger.log("Could not delete file", e);
            }
        }
    }

    public static void loadConfigForField(Field field, ConfigurationSection section, Object target) {
        if (!field.isAnnotationPresent(ConfigurationField.class))
            return;
        String location = field.getAnnotation(ConfigurationField.class).value() + "." + field.getName();
        if (field.getType().equals(ConfigurationSection.class)) {
            ConfigurationSection newSection = section.getConfigurationSection(location);
            if (newSection == null)
                newSection = section.createSection(location);
            ReflectionManager.setValue(field, newSection, target);
        } else {
            Object value = Objects.requireNonNull(section).get(location);
            if (value != null)
                ReflectionManager.setValue(field, value, target);
            else
                section.set(location, ReflectionManager.getValue(field, target));
        }
    }

    public static void saveConfiguration(File config, FileConfiguration configuration) {
        try {
            deleteFile(config);
            Files.write(config.toPath(), Charset.defaultCharset().encode(configuration.saveToString()).array());
        } catch (IOException e) {
            EnchantLogger.log(Level.SEVERE, "Could not save configuration", e);
        }
    }

    public static void saveConfigurationGuide(EnchantmentTokens main, String path) {
        File configGuide = new File(path);
        if (!configGuide.exists()) {
            Bukkit.getScheduler().runTaskAsynchronously(main, () -> writeInternalFile(path, "configurationguide.txt"));
        }
    }

    public static void writeInternalFile(String path, String name) {
        try(OutputStream out = new FileOutputStream(path)) {
            InputStream stream = Objects.requireNonNull(ConfigurationManager.class.getClassLoader().getResourceAsStream(name));
            int readBytes;
            byte[] buffer = new byte[4096];
            while ((readBytes = stream.read(buffer)) > 0) {
                out.write(buffer, 0, readBytes);
            }
            stream.close();
        } catch (IOException e) {
            EnchantLogger.log(Level.SEVERE, "Could not create new configurationguide file!");
        }
    }

    public static void createFolder(String path) {
        File data = new File(path);
        if (!data.exists() && !data.mkdir())
            EnchantLogger.log(Level.SEVERE, "[ERROR] Could not create folder {0}", path);
    }

    public static File getFolder(String path) {
        File folder = new File(path);
        createFolder(folder);
        return folder;
    }

    public static void createFolder(File file) {
        if (!file.exists() && !file.mkdir())
            EnchantLogger.log(Level.SEVERE, "[ERROR] Could not create folder {0}", file.getPath());
    }

    public static void createFile(File file) {
        try {
            if (!file.exists() && !file.createNewFile())
                EnchantLogger.log(Level.SEVERE, "[ERROR] Could not create file {0}", file.getPath());
        } catch (IOException e) {
            EnchantLogger.log(Level.SEVERE, "[ERROR] Could not access {0}", file.getPath());
        }
    }

    public static ConfigurationSection getSectionOrCreate(ConfigurationSection section, String subsection) {
        ConfigurationSection found = section.getConfigurationSection(subsection);
        if (found == null)
            return section.createSection(subsection);
        return found;
    }
}
