/*
 * Custom enchantments for Minecraft
 * Copyright (C) 2021 Big_Bad_E
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import com.bigbade.enchantmenttokens.EnchantmentTokens;
import com.bigbade.enchantmenttokens.configuration.ConfigurationManager;
import com.bigbade.enchantmenttokens.configuration.ConfigurationType;
import com.bigbade.enchantmenttokens.currency.CurrencyFactory;
import com.bigbade.enchantmenttokens.utils.FileHelper;
import com.bigbade.enchantmenttokens.utils.ReflectionManager;
import com.bigbade.enchantmenttokens.utils.enchants.EnchantmentFileLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.logging.Level;

public class CurrencyFactoryHandler {
    private final EnchantmentTokens main;
    private final ConfigurationSection section;

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
            return loadExternalFactory();
        }
    }

    private CurrencyFactory loadExternalFactory() {
        CurrencyFactory factory = loadExternalJar();
        if (factory != null && factory.loaded()) {
            return factory;
        } else {
            if (factory == null) {
                EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not find currency factory");
            } else {
                EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not load currency factory");
            }
            return loadGemFactory();
        }
    }

    private CurrencyFactory loadGemFactory() {
        if (ReflectionManager.VERSION >= 14) {
            boolean persistent = new ConfigurationType<>(true).getValue("usePersistentData", section);
            if (persistent) {
                return new LatestCurrencyFactory(new NamespacedKey(main, "gems"), new NamespacedKey(main, "locale"));
            } else {
                return new GemCurrencyFactory(main.getScheduler(), main.getDataFolder().getAbsolutePath());
            }
        } else {
            return new GemCurrencyFactory(main.getScheduler(), main.getDataFolder().getAbsolutePath());
        }
    }

    private CurrencyFactory loadExternalJar() {
        File found = ConfigurationManager.getFolder(main.getDataFolder().getAbsolutePath() + "\\storage");
        if (found.listFiles() == null) {
            return null;
        }
        String type = new ConfigurationType<>("gems").getValue("type", section);
        for (File subfile : Objects.requireNonNull(found.listFiles())) {
            if (!subfile.getName().endsWith(".jar")) {
                continue;
            }
            CurrencyFactory factory = loadFactory(type, subfile);
            if (factory != null) {
                return factory;
            }
        }
        return null;
    }

    private CurrencyFactory loadFactory(String type, File file) {
        try (JarFile jarFile = FileHelper.getJarFile(file.getAbsolutePath());
             InputStream stream = FileHelper.getJarStream(jarFile, "config.yml")) {
            FileConfiguration configuration = ConfigurationManager.loadConfigurationStream(stream);

            if (!new ConfigurationType<>("gems").getValue("name", configuration)
                    .equalsIgnoreCase(type)) {
                return null;
            }

            Set<Class<CurrencyFactory>> classes = EnchantmentFileLoader.loadClassesForFile(file,
                    getClass().getClassLoader(), CurrencyFactory.class);
            Iterator<Class<CurrencyFactory>> iterator = classes.iterator();
            if (iterator.hasNext()) {
                return getFactory(iterator.next());
            }
        } catch (IOException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE,
                    "Could not load currency handler (is it valid?)", e);
        }
        return null;
    }

    private CurrencyFactory getFactory(Class<CurrencyFactory> clazz) {
        return ReflectionManager.instantiate(Objects.requireNonNull(ReflectionManager.getConstructor(clazz)), section);
    }
}
