package software.bigbade.enchantmenttokens.utils.configuration;

/*
EnchantmentTokens
Copyright (C) 2019-2020 Big_Bad_E
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

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
import java.nio.file.Files;
import java.util.Objects;
import java.util.logging.Level;

public class ConfigurationManager {
    private ConfigurationManager() { }

    public static FileConfiguration loadConfigurationFile(String path) {
        File config = new File(path);

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

    public static void saveConfiguration(String path, FileConfiguration configuration) {
        File config = new File(path);
        try {
            deleteFile(config);
            Files.write(config.toPath(), configuration.saveToString().getBytes());
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
