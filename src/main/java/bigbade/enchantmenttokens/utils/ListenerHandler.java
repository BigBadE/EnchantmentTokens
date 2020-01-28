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
import bigbade.enchantmenttokens.api.EnchantListener;
import bigbade.enchantmenttokens.api.EnchantmentBase;
import bigbade.enchantmenttokens.api.ListenerType;
import bigbade.enchantmenttokens.events.EnchantmentEvent;
import bigbade.enchantmenttokens.listeners.InventoryMoveListener;
import bigbade.enchantmenttokens.listeners.enchants.ArmorEquipListener;
import bigbade.enchantmenttokens.listeners.enchants.BlockBreakListener;
import bigbade.enchantmenttokens.listeners.enchants.BlockDamageListener;
import bigbade.enchantmenttokens.listeners.enchants.EnchantmentListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class ListenerHandler {
    private Map<ListenerType, ListenerManager> enchantListeners = new HashMap<>();
    private EnchantmentTokens main;

    public ListenerHandler(EnchantmentTokens main) {
        this.main = main;
    }

    public Map<ListenerType, ListenerManager> getEnchantListeners() {
        return enchantListeners;
    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(enchantListeners.get(ListenerType.BLOCKBREAK), main), main);
        Bukkit.getPluginManager().registerEvents(new ArmorEquipListener(enchantListeners.get(ListenerType.EQUIP), enchantListeners.get(ListenerType.UNEQUIP)), main);
        Bukkit.getPluginManager().registerEvents(new BlockDamageListener(enchantListeners.get(ListenerType.BLOCKDAMAGED)), main);
        Bukkit.getPluginManager().registerEvents(new InventoryMoveListener(enchantListeners.get(ListenerType.HELD), enchantListeners.get(ListenerType.SWAPPED), main), main);
    }


    public List<EnchantmentBase> loadEnchantments(Map<String, Set<Class<EnchantmentBase>>> enchants) {
        List<EnchantmentBase> enchantments = new ArrayList<>();

        for (ListenerType type : ListenerType.values()) {
            enchantListeners.put(type, new ListenerManager());
        }

        ReflectionManager.setValue(ReflectionManager.getField(Enchantment.class, "acceptingNew"), true, Enchantment.class);

        for (Map.Entry<String, Set<Class<EnchantmentBase>>> entry : enchants.entrySet()) {
            for (Class<EnchantmentBase> clazz : entry.getValue()) {
                FileConfiguration configuration = ConfigurationManager.loadConfigurationFile(main.getDataFolder().getPath() + "\\enchantments\\" + entry.getKey() + ".yml");

                ConfigurationSection section = configuration.getConfigurationSection("enchants");
                if (section == null)
                    section = configuration.createSection("enchants");
                    String name = clazz.getSimpleName();
                    ConfigurationSection enchantSection = configuration.getConfigurationSection(name);
                    final EnchantmentBase enchant = (EnchantmentBase) ReflectionManager.instantiate(clazz);
                    if (enchantSection == null) {
                        enchantSection = section.createSection(name.toLowerCase());
                        enchantSection.set("enabled", true);
                    }

                    for (Field field : clazz.getDeclaredFields()) {
                        ConfigurationManager.loadConfigForField(field, enchantSection, enchant);
                    }
                    for (Field field : clazz.getSuperclass().getDeclaredFields()) {
                        ConfigurationManager.loadConfigForField(field, enchantSection, enchant);
                    }

                    boolean enabled;
                    if(enchantSection.isSet("enabled"))
                        enabled = enchantSection.getBoolean("enabled");
                    else {
                        enabled = true;
                        enchantSection.set("enabled", true);
                    }

                    if (enabled) {
                        assert enchant != null;
                        enchant.loadConfig();
                        enchantments.add(enchant);
                        methodCheck:
                        for (Method method : clazz.getDeclaredMethods()) {
                            if (method.isAnnotationPresent(EnchantListener.class) && method.getReturnType() == EnchantmentListener.class) {
                                ListenerType type = method.getAnnotation(EnchantListener.class).type();
                                if (enchant.getItemTarget() != null) {
                                    if (!type.canTarget(enchant.getItemTarget())) {
                                        main.getLogger().warning("Cannot add listener " + type + " to target " + enchant.getItemTarget());
                                        continue;
                                    }
                                } else
                                    for (Material material : enchant.getTargets()) {
                                        if (!type.canTarget(material)) {
                                            main.getLogger().warning("Cannot add listener " + type + " to target " + material);
                                        }
                                        continue methodCheck;
                                    }
                                enchantListeners.get(type).add((EnchantmentListener<EnchantmentEvent<? extends Event>>) ReflectionManager.invoke(method, enchant), enchant);
                            }
                        }
                        Enchantment.registerEnchantment(enchant);
                    }
                ConfigurationManager.saveConfiguration(main.getDataFolder().getPath() + "\\enchantments\\" + entry.getKey() + ".yml", configuration);
            }
        }
        return enchantments;
    }
}
