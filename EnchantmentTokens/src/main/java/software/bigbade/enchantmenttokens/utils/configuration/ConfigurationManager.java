package software.bigbade.enchantmenttokens.utils.configuration;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import software.bigbade.enchantmenttokens.api.ConfigurationField;
import software.bigbade.enchantmenttokens.utils.EnchantLogger;
import software.bigbade.enchantmenttokens.utils.ReflectionManager;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.logging.Level;

public class ConfigurationManager {
    //Private constructor to hide inherent public constructor
    private ConfigurationManager() { }

    /**
     * Loads YamlConfiguration from file
     * @param config File object of the .yml file
     * @return YamlConfiguration instance of the file
     */
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

    /**
     * Loads YamlConfiguration from stream, used for compressed config files.
     * @param stream InputStream of file.
     * @return YamlConfiguration instance of stream.
     *
     * @see ConfigurationManager#loadConfigurationFile(File)
     */
    public static FileConfiguration loadConfigurationStream(InputStream stream) {
        FileConfiguration configuration = new YamlConfiguration();
        try {
            configuration.load(new InputStreamReader(stream, Charset.defaultCharset()));
        } catch (IOException | InvalidConfigurationException e) {
            EnchantLogger.log(Level.SEVERE, "could not load enchantment configuration", e);
        }
        return configuration;
    }

    /**
     * Deletes file and catches exceptions
     * @param file file to delete
     *
     * @see Files#delete(Path)
     * @see File#delete()
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            try {
                Files.delete(file.toPath());
            } catch (IOException e) {
                EnchantLogger.log("Could not delete file", e);
            }
        }
    }

    /**
     * Loads configuration field at path given by location
     * @param field The field to check and load the value in
     * @param section The section to read/write to
     * @param target Object target
     *
     * @see ConfigurationField
     */
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

    /**
     * Saved FileConfiguration from memory to File and catches exceptions
     * @param file File to save configuration to
     * @param configuration FileConfiguration to save from memory
     */
    public static void saveConfiguration(File file, FileConfiguration configuration) {
        try {
            deleteFile(file);
            Files.write(file.toPath(), Charset.defaultCharset().encode(configuration.saveToString()).array());
        } catch (IOException e) {
            EnchantLogger.log(Level.SEVERE, "Could not save configuration", e);
        }
    }

    /**
     * Saves ConfigurationGuide.txt
     * @param scheduler SchedulerHandler to schedule the task with
     * @param path Path to the file
     */
    public static void saveConfigurationGuide(SchedulerHandler scheduler, String path) {
        File configGuide = new File(path + "\\ConfigurationGuide.txt");
        if (!configGuide.exists()) {
            scheduler.runTaskAsync(() -> writeInternalFile(path, "ConfigurationGuide.txt"));
        }
    }

    /**
     * Writes loaded resource file to path
     * @param path Path of the file
     * @param name Name of the resource
     *
     * @see ConfigurationManager#saveConfigurationGuide(SchedulerHandler, String)
     */
    public static void writeInternalFile(String path, String name) {
        try(OutputStream out = new FileOutputStream(path + "/" + name); InputStream stream = Objects.requireNonNull(ConfigurationManager.class.getClassLoader().getResourceAsStream(name))) {
            int readBytes;
            byte[] buffer = new byte[4096];
            while ((readBytes = stream.read(buffer)) > 0) {
                out.write(buffer, 0, readBytes);
            }
        } catch (IOException e) {
            EnchantLogger.log(Level.SEVERE, "Could not create new ConfigurationGuide file!");
        }
    }

    /**
     * Creates folder and catches exceptions
     * @param path Path to save folder to
     */
    public static void createFolder(String path) {
        File data = new File(path);
        if (!data.exists() && !data.mkdir())
            EnchantLogger.log(Level.SEVERE, "[ERROR] Could not create folder {0}", path);
    }

    /**
     * Gets folder or creates it if one isn't found
     * @param path Path to the folder
     * @return File instance of the folder
     */
    public static File getFolder(String path) {
        File folder = new File(path);
        createFolder(folder);
        return folder;
    }

    /**
     * Checks if folder exists, and if it doesn't safely creates folder there.
     * @param file File instance of the folder
     */
    public static void createFolder(File file) {
        if (!file.exists() && !file.mkdir())
            EnchantLogger.log(Level.SEVERE, "[ERROR] Could not create folder {0}", file.getPath());
    }

    /**
     * Checks if file exists, and if it doesn't safely creates file there.
     * @param file File instance
     */
    public static void createFile(File file) {
        try {
            if (!file.exists() && !file.createNewFile())
                EnchantLogger.log(Level.SEVERE, "[ERROR] Could not create file {0}", file.getPath());
        } catch (IOException e) {
            EnchantLogger.log(Level.SEVERE, "[ERROR] Could not access {0}", file.getPath());
        }
    }

    /**
     * Gets section, or creates it if it is not found
     * @param section Section to look in
     * @param subsection Name of subsection
     * @return ConfigurationSection instance of subsection
     */
    public static ConfigurationSection getSectionOrCreate(ConfigurationSection section, String subsection) {
        ConfigurationSection found = section.getConfigurationSection(subsection);
        if (found == null)
            return section.createSection(subsection);
        return found;
    }
}
