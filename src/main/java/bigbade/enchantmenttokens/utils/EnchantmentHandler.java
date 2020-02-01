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
import bigbade.enchantmenttokens.api.EnchantmentBase;
import bigbade.enchantmenttokens.api.VanillaEnchant;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;

public class EnchantmentHandler {
    private List<EnchantmentBase> enchantments = new ArrayList<>();
    private List<VanillaEnchant> vanillaEnchants = new ArrayList<>();

    private EnchantmentTokens main;

    public EnchantmentHandler(EnchantmentTokens main) {
        this.main = main;
    }

    public void registerEnchants(Collection<EnchantmentBase> enchantments) {
        List<Enchantment> enchantsToRegister = new ArrayList<>();
        ConfigurationSection section = main.getConfig().getConfigurationSection("enchants");
        if (section == null)
            section = main.getConfig().createSection("enchants");

        for (String name : section.getStringList("vanillaEnchants")) {
            Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(name.toLowerCase().replace(" ", "_")));
            if (enchantment != null) enchantsToRegister.add(enchantment);
            else main.getLogger().log(Level.SEVERE, "Could not find an enchantment by the name " + name);
        }

        for (Enchantment enchantment : enchantsToRegister) {
            ConfigurationSection enchantSection = section.getConfigurationSection(enchantment.getKey().getKey());
            if (enchantSection == null)
                enchantSection = section.createSection(enchantment.getKey().getKey());
            String iconName = enchantSection.getString("icon");
            if (iconName == null)
                iconName = "BEDROCK";
            Material icon = Material.getMaterial(iconName);

            if ((boolean) ConfigurationManager.getValueOrDefault("enabled", enchantSection, true))
                vanillaEnchants.add(new VanillaEnchant(icon, enchantment));
        }

        main.saveConfig();

        for (EnchantmentBase enchantment : enchantments) {
            this.enchantments.add(enchantment);
            Enchantment.registerEnchantment(enchantment);
        }
        main.getLogger().log(Level.INFO, "Registered enchantments");
    }

    public void unregisterEnchants() {
        Field byKey = ReflectionManager.getField(Enchantment.class, "byKey");
        Field byName = ReflectionManager.getField(Enchantment.class, "byName");

        ReflectionManager.removeFinalFromField(byKey);
        ReflectionManager.removeFinalFromField(byName);

        Map<NamespacedKey, Enchantment> byKeys = (HashMap<NamespacedKey, Enchantment>) ReflectionManager.getValue(byKey, null);
        assert byKeys != null;
        for (Enchantment enchantment : enchantments) {
            byKeys.remove(enchantment.getKey());
        }
        ReflectionManager.setValue(byKey, byKeys, null);
        Map<NamespacedKey, String> byNames = (HashMap<NamespacedKey, String>) ReflectionManager.getValue(byName, null);
        assert byNames != null;
        for (Enchantment enchantment : enchantments) {
            byNames.remove(enchantment.getKey());
        }
        ReflectionManager.setValue(byName, byNames, null);
    }

    public List<EnchantmentBase> getEnchantments() {
        return enchantments;
    }

    public List<VanillaEnchant> getVanillaEnchants() {
        return vanillaEnchants;
    }
}
