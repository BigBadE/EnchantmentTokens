package bigbade.enchantmenttokens.utils;

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

import bigbade.enchantmenttokens.EnchantmentTokens;
import bigbade.enchantmenttokens.api.ConfigurationField;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.Objects;
import java.util.logging.Level;

public class ConfigurationManager {
    public static FileConfiguration loadConfigurationFile(String path) {
        File config = new File(path);

        FileConfiguration configuration = new YamlConfiguration();
        try {
            if (!config.exists())
                if (!config.createNewFile())
                    EnchantmentTokens.LOGGER.log(Level.SEVERE, "Problem creating config file at " + config.getPath());
            configuration.load(config);
        } catch (IOException | InvalidConfigurationException e) {
            EnchantmentTokens.LOGGER.log(Level.SEVERE, "could not load enchantment configuration", e);
        }
        return configuration;
    }

    public static void loadConfigForField(Field field, ConfigurationSection section, Object target) {
        if (field.isAnnotationPresent(ConfigurationField.class)) {
            String location = field.getAnnotation(ConfigurationField.class).value();
            ConfigurationSection current = section;
            if (location.contains(".")) {
                String[] next = location.split("\\.");
                for (String nextLoc : next) {
                    try {
                        ConfigurationSection newSection = Objects.requireNonNull(current).getConfigurationSection(nextLoc);
                        if (newSection == null)
                            current = current.createSection(nextLoc);
                        else
                            current = newSection;
                    } catch (NullPointerException ignored) {
                        assert current != null;
                        current = current.createSection(nextLoc);
                    }
                }
            }
            location = field.getName();
            if (field.getType().equals(ConfigurationSection.class)) {
                ConfigurationSection newSection = current.getConfigurationSection(location);
                if (newSection == null)
                    newSection = current.createSection(location);
                ReflectionManager.setValue(field, newSection, target);
            } else {
                Object value = Objects.requireNonNull(current).get(location);
                if (value != null)
                    ReflectionManager.setValue(field, value, target);
                else
                    current.set(location, ReflectionManager.getValue(field, target));
            }
        }
    }

    public static void saveConfiguration(String path, FileConfiguration configuration) {
        File config = new File(path);
        try {
            if (config.exists() && !config.delete())
                EnchantmentTokens.LOGGER.log(Level.SEVERE, "Could not save configuration");
            Files.write(config.toPath(), configuration.saveToString().getBytes());
        } catch (IOException e) {
            EnchantmentTokens.LOGGER.log(Level.SEVERE, "Could not save configuration", e);
        }
    }

    public static void saveConfigurationGuide(EnchantmentTokens main, String path, ClassLoader loader) {
        File configGuide = new File(path);
        if (!configGuide.exists()) {
            Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
                try {
                    InputStream stream = Objects.requireNonNull(loader.getResourceAsStream("configurationguide.txt"));
                    byte[] data = new byte[stream.available()];
                    Files.write(configGuide.toPath(), data);
                } catch (IOException e) {
                    EnchantmentTokens.LOGGER.log(Level.SEVERE, "Could not create new configurationguide file!");
                }
            });
        }
    }
}
