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

package com.bigbade.enchantmenttokens.utils.enchants;

import com.bigbade.enchantmenttokens.EnchantmentTokens;
import com.bigbade.enchantmenttokens.api.CustomEnchantment;
import com.bigbade.enchantmenttokens.api.EnchantmentBase;
import com.bigbade.enchantmenttokens.api.SkriptEnchantments;
import com.bigbade.enchantmenttokens.api.VanillaEnchant;
import com.bigbade.enchantmenttokens.configuration.ConfigurationManager;
import com.bigbade.enchantmenttokens.configuration.ConfigurationType;
import com.bigbade.enchantmenttokens.utils.ReflectionManager;
import com.bigbade.enchantmenttokens.utils.RegexPatterns;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;

import javax.annotation.Nullable;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;

public class CustomEnchantmentHandler implements EnchantmentHandler {

    @SuppressWarnings("unchecked")
    private static final Map<NamespacedKey, Enchantment> KEY_ENCHANTMENT_MAP = (Map<NamespacedKey, Enchantment>)
            ReflectionManager.getValue(ReflectionManager.getField(Enchantment.class, "byKey"), null);
    @SuppressWarnings("unchecked")
    private static final Map<String, Enchantment> NAME_ENCHANTMENT_MAP = (Map<String, Enchantment>)
            ReflectionManager.getValue(ReflectionManager.getField(Enchantment.class, "byName"), null);
    private final List<EnchantmentBase> enchantments = new ArrayList<>();
    private final List<VanillaEnchant> vanillaEnchants = new ArrayList<>();
    private final List<EnchantmentBase> allEnchants = new ArrayList<>();
    private final FileConfiguration config;

    @Nullable
    private final SkriptEnchantmentHandler skriptManager;

    public CustomEnchantmentHandler(FileConfiguration config, String skriptPath) {
        this.config = config;
        if(Bukkit.getServer().getPluginManager().getPlugin("Skript") != null) {
            skriptManager = new SkriptEnchantmentHandler(
                    this, ConfigurationManager.loadConfigurationFile(new File(skriptPath)));
        } else {
            skriptManager = null;
        }
    }

    private static void registerBaseToMaps(EnchantmentBase base) {
        Objects.requireNonNull(CustomEnchantmentHandler.KEY_ENCHANTMENT_MAP);
        if (CustomEnchantmentHandler.KEY_ENCHANTMENT_MAP.containsKey(base.getKey())) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE,
                    "Duplicate key {0} found, make sure the same addon jar is not copy pasted!", base.getKey());
        } else {
            Objects.requireNonNull(CustomEnchantmentHandler.NAME_ENCHANTMENT_MAP);
            if (CustomEnchantmentHandler.NAME_ENCHANTMENT_MAP.containsKey(base.getEnchantmentName())) {
                NamespacedKey old = CustomEnchantmentHandler.NAME_ENCHANTMENT_MAP.get(base.getEnchantmentName())
                        .getKey();
                EnchantmentTokens.getEnchantLogger().log(Level.SEVERE,
                        "Duplicate enchantment names {0} and {1}, skipping (WILL CAUSE ERRORS)",
                        new Object[]{base.getKey(), old});
                return;
            }
            CustomEnchantmentHandler.KEY_ENCHANTMENT_MAP.put(base.getKey(), base.getEnchantment());
            CustomEnchantmentHandler.NAME_ENCHANTMENT_MAP.put(base.getEnchantmentName(), base.getEnchantment());
        }
    }

    public void registerEnchants(Collection<EnchantmentBase> enchantments) {
        ConfigurationSection section = ConfigurationManager.getSectionOrCreate(config, "enchants");

        for (String name : new ConfigurationType<>(Collections.singletonList("Fortune"))
                .getValue("vanilla-enchants", section)) {
            registerVanillaEnchantment(name, section);
        }

        this.enchantments.addAll(enchantments);
        registerEnchantments(enchantments);

        allEnchants.addAll(enchantments);
        allEnchants.addAll(vanillaEnchants);

        if(skriptManager != null) {
            skriptManager.registerEnchantments();
        }

        EnchantmentTokens.getEnchantLogger().log(Level.INFO, "Registered enchantments");
    }

    public void registerEnchant(EnchantmentBase base) {
        enchantments.add(base);
        allEnchants.add(base);
        CustomEnchantmentHandler.registerBaseToMaps(base);
    }

    private void registerVanillaEnchantment(String name, ConfigurationSection section) {
        Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(
                RegexPatterns.SPACE_PATTERN.matcher(name.toLowerCase()).replaceAll( "_")));
        if (enchantment != null) {
            loadVanillaConfig(enchantment, section);
        } else {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE,
                    "Could not find an enchantment by the name {0}", name);
        }
    }

    public static void registerEnchantments(Collection<EnchantmentBase> enchantments) {
        for (EnchantmentBase base : enchantments) {
            CustomEnchantmentHandler.registerBaseToMaps(base);
        }
    }

    private void loadVanillaConfig(Enchantment enchantment, ConfigurationSection section) {
        ConfigurationSection enchantSection = section.getConfigurationSection(enchantment.getKey().getKey());
        if (enchantSection == null) {
            enchantSection = section.createSection(enchantment.getKey().getKey());
        }

        if (new ConfigurationType<>(true).getValue("enabled", enchantSection)) {
            VanillaEnchant vanillaEnchant = new VanillaEnchant(enchantment, false);
            vanillaEnchants.add(vanillaEnchant);
            for (Field field : CustomEnchantment.class.getDeclaredFields()) {
                ConfigurationManager.loadConfigForField(field, enchantSection, vanillaEnchant);
            }
            vanillaEnchant.loadConfig();
        }
    }

    @SuppressWarnings("unchecked")
    public void unregisterEnchants() {
        Field byKey = ReflectionManager.getField(Enchantment.class, "byKey");
        Field byName = ReflectionManager.getField(Enchantment.class, "byName");

        Map<NamespacedKey, Enchantment> byKeys = (HashMap<NamespacedKey, Enchantment>)
                ReflectionManager.getValue(byKey, null);
        for (EnchantmentBase enchantment : enchantments) {
            byKeys.remove(enchantment.getKey());
        }

        Map<NamespacedKey, String> byNames = (HashMap<NamespacedKey, String>)
                ReflectionManager.getValue(byName, null);
        for (EnchantmentBase enchantment : enchantments) {
            byNames.remove(enchantment.getKey());
        }
    }


    @Override
    public boolean hasVanillaEnchant(Enchantment enchantment) {
        for (EnchantmentBase base : vanillaEnchants) {
            if (base.getKey().equals(enchantment.getKey())) {
                return true;
            }
        }
        return false;
    }

    public List<EnchantmentBase> getCustomEnchants() {
        return enchantments;
    }

    @Override
    public Optional<SkriptEnchantments> getSkriptEnchantmentHandler() {
        return Optional.ofNullable(skriptManager);
    }

    public List<EnchantmentBase> getAllEnchants() {
        return allEnchants;
    }
}
