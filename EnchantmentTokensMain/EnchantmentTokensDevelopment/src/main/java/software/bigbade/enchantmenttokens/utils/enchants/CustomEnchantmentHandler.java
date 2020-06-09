/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.utils.enchants;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.CustomEnchantment;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.VanillaEnchant;
import software.bigbade.enchantmenttokens.configuration.ConfigurationManager;
import software.bigbade.enchantmenttokens.configuration.ConfigurationType;
import software.bigbade.enchantmenttokens.utils.ReflectionManager;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

public class CustomEnchantmentHandler implements EnchantmentHandler {
    private final List<EnchantmentBase> enchantments = new ArrayList<>();
    private final List<VanillaEnchant> vanillaEnchants = new ArrayList<>();
    private final List<EnchantmentBase> skriptEnchantments = new ArrayList<>();
    private final List<EnchantmentBase> allEnchants = new ArrayList<>();

    private final FileConfiguration config;
    private final FileConfiguration skriptConfiguration;
    private final String skriptPath;


    @SuppressWarnings("unchecked")
    private static final Map<NamespacedKey, Enchantment> KEY_ENCHANTMENT_MAP = (Map<NamespacedKey, Enchantment>) ReflectionManager.getValue(ReflectionManager.getField(Enchantment.class, "byKey"), null);
    @SuppressWarnings("unchecked")
    private static final Map<String, Enchantment> NAME_ENCHANTMENT_MAP = (Map<String, Enchantment>) ReflectionManager.getValue(ReflectionManager.getField(Enchantment.class, "byName"), null);

    public CustomEnchantmentHandler(FileConfiguration config, String skriptPath) {
        this.config = config;
        this.skriptPath = skriptPath;
        skriptConfiguration = ConfigurationManager.loadConfigurationFile(new File(skriptPath));
    }

    public void registerEnchants(Collection<EnchantmentBase> enchantments) {
        ConfigurationSection section = ConfigurationManager.getSectionOrCreate(config, "enchants");

        for (String name : new ConfigurationType<>(Collections.singletonList("Fortune")).getValue("vanilla-enchants", section)) {
            registerVanillaEnchantment(name, section);
        }

        this.enchantments.addAll(enchantments);
        registerEnchantments(enchantments);

        registerEnchantments(skriptEnchantments);

        allEnchants.addAll(enchantments);
        allEnchants.addAll(vanillaEnchants);

        ConfigurationManager.saveConfiguration(new File(skriptPath), skriptConfiguration);

        EnchantmentTokens.getEnchantLogger().log(Level.INFO, "Registered enchantments");
    }

    public void registerEnchant(EnchantmentBase base) {
        enchantments.add(base);
        allEnchants.add(base);
        registerBaseToMaps(base);
    }

    private void registerVanillaEnchantment(String name, ConfigurationSection section) {
        Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(name.toLowerCase().replace(" ", "_")));
        if (enchantment != null) loadVanillaConfig(enchantment, section);
        else
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not find an enchantment by the name {0}", name);
    }

    private void registerEnchantments(Collection<EnchantmentBase> enchantments) {
        for (EnchantmentBase base : enchantments) {
            registerBaseToMaps(base);
        }
    }

    private void registerBaseToMaps(EnchantmentBase base) {
        Objects.requireNonNull(CustomEnchantmentHandler.KEY_ENCHANTMENT_MAP);
        if (CustomEnchantmentHandler.KEY_ENCHANTMENT_MAP.containsKey(base.getKey())) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Duplicate key {0} found, make sure the same addon jar is not copy pasted!", base.getKey());
        } else {
            Objects.requireNonNull(CustomEnchantmentHandler.NAME_ENCHANTMENT_MAP);
            if (CustomEnchantmentHandler.NAME_ENCHANTMENT_MAP.containsKey(base.getEnchantmentName())) {
                NamespacedKey old = CustomEnchantmentHandler.NAME_ENCHANTMENT_MAP.get(base.getEnchantmentName()).getKey();
                EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Duplicate enchantment names {0} and {1}, skipping (WILL CAUSE ERRORS)", new Object[]{base.getKey(), old});
                return;
            }
            CustomEnchantmentHandler.KEY_ENCHANTMENT_MAP.put(base.getKey(), base.getEnchantment());
            CustomEnchantmentHandler.NAME_ENCHANTMENT_MAP.put(base.getEnchantmentName(), base.getEnchantment());
        }
    }

    private void loadVanillaConfig(Enchantment enchantment, ConfigurationSection section) {
        ConfigurationSection enchantSection = section.getConfigurationSection(enchantment.getKey().getKey());
        if (enchantSection == null)
            enchantSection = section.createSection(enchantment.getKey().getKey());

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

        Map<NamespacedKey, Enchantment> byKeys = (HashMap<NamespacedKey, Enchantment>) ReflectionManager.getValue(byKey, null);
        for (EnchantmentBase enchantment : enchantments) {
            byKeys.remove(enchantment.getKey());
        }

        Map<NamespacedKey, String> byNames = (HashMap<NamespacedKey, String>) ReflectionManager.getValue(byName, null);
        for (EnchantmentBase enchantment : enchantments) {
            byNames.remove(enchantment.getKey());
        }
    }

    public void addSkriptEnchant(EnchantmentBase enchantment) {
        for (Field field : enchantment.getClass().getSuperclass().getDeclaredFields())
            ConfigurationManager.loadConfigForField(field, ConfigurationManager.getSectionOrCreate(skriptConfiguration, enchantment.getKey().getKey()), enchantment);
        enchantment.loadConfig();
        skriptEnchantments.add(enchantment);
        allEnchants.add(enchantment);
    }

    @Override
    public boolean hasVanillaEnchant(Enchantment enchantment) {
        for(EnchantmentBase base : vanillaEnchants) {
            if(base.getKey().equals(enchantment.getKey())) {
                return true;
            }
        }
        return false;
    }

    public List<EnchantmentBase> getCustomEnchants() {
        return enchantments;
    }

    public List<EnchantmentBase> getSkriptEnchant() {
        return skriptEnchantments;
    }

    public List<EnchantmentBase> getAllEnchants() {
        return allEnchants;
    }
}
