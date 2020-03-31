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
    private List<EnchantmentBase> enchantments = new ArrayList<>();
    private List<VanillaEnchant> vanillaEnchants = new ArrayList<>();
    private List<EnchantmentBase> skriptEnchantments = new ArrayList<>();
    private List<EnchantmentBase> allEnchants = new ArrayList<>();

    private FileConfiguration config;
    private FileConfiguration skriptConfiguration;
    private String skriptPath;

    public CustomEnchantmentHandler(FileConfiguration config, String skriptPath) {
        this.config = config;
        this.skriptPath = skriptPath;
        skriptConfiguration = ConfigurationManager.loadConfigurationFile(new File(skriptPath));
    }

    public void registerEnchants(Collection<EnchantmentBase> enchantments) {
        List<Enchantment> vanillaRegistering = new ArrayList<>();
        ConfigurationSection section = ConfigurationManager.getSectionOrCreate(config, "enchants");

        for (String name : new ConfigurationType<>(Collections.singletonList("Fortune")).getValue("vanillaEnchants", section)) {
            Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(name.toLowerCase().replace(" ", "_")));
            if (enchantment != null) vanillaRegistering.add(enchantment);
            else
                EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not find an enchantment by the name {0}", name);
        }

        vanillaRegistering.forEach(enchantment -> loadVanillaConfig(enchantment, section));

        this.enchantments.addAll(enchantments);
        registerEnchantments(enchantments);

        registerEnchantments(skriptEnchantments);

        allEnchants.addAll(enchantments);
        allEnchants.addAll(vanillaEnchants);

        ConfigurationManager.saveConfiguration(new File(skriptPath), skriptConfiguration);

        EnchantmentTokens.getEnchantLogger().log(Level.INFO, "Registered enchantments");
    }

    @SuppressWarnings("unchecked")
    private void registerEnchantments(Collection<EnchantmentBase> enchantments) {
        Map<NamespacedKey, Enchantment> keyEnchantmentMap = ((Map<NamespacedKey, Enchantment>) ReflectionManager.getValue(ReflectionManager.getField(Enchantment.class, "byKey"), null));
        Map<String, Enchantment> nameEnchantmentMap = ((Map<String, Enchantment>) ReflectionManager.getValue(ReflectionManager.getField(Enchantment.class, "byName"), null));

        for (EnchantmentBase base : enchantments) {
            Objects.requireNonNull(keyEnchantmentMap);
            if (keyEnchantmentMap.containsKey(base.getKey())) {
                EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Duplicate key {0} found, make sure the same addon jar is not copy pasted!", base.getKey());
            } else {
                Objects.requireNonNull(nameEnchantmentMap);
                if (nameEnchantmentMap.containsKey(base.getEnchantmentName())) {
                    NamespacedKey old = nameEnchantmentMap.get(base.getEnchantmentName()).getKey();
                    EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Duplicate enchantments {0} and {1}, replacing old one (MAY CAUSE ERRORS)", new Object[]{base.getKey(), old});
                    keyEnchantmentMap.remove(old);
                    nameEnchantmentMap.remove(base.getEnchantmentName());
                }
                keyEnchantmentMap.put(base.getKey(), base.getEnchantment());
                nameEnchantmentMap.put(base.getEnchantmentName(), base.getEnchantment());
            }
        }
    }

    private void loadVanillaConfig(Enchantment enchantment, ConfigurationSection section) {
        ConfigurationSection enchantSection = section.getConfigurationSection(enchantment.getKey().getKey());
        if (enchantSection == null)
            enchantSection = section.createSection(enchantment.getKey().getKey());

        if (new ConfigurationType<>(true).getValue("enabled", enchantSection)) {
            VanillaEnchant vanillaEnchant = new VanillaEnchant(enchantment);
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
        assert byKeys != null;
        for (EnchantmentBase enchantment : enchantments) {
            byKeys.remove(enchantment.getKey());
        }

        Map<NamespacedKey, String> byNames = (HashMap<NamespacedKey, String>) ReflectionManager.getValue(byName, null);
        assert byNames != null;
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
