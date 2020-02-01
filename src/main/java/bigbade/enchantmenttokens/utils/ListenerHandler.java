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
import bigbade.enchantmenttokens.api.EnchantmentAddon;
import bigbade.enchantmenttokens.api.EnchantmentBase;
import bigbade.enchantmenttokens.api.ListenerType;
import bigbade.enchantmenttokens.events.EnchantmentApplyEvent;
import bigbade.enchantmenttokens.events.EnchantmentEvent;
import bigbade.enchantmenttokens.listeners.*;
import bigbade.enchantmenttokens.listeners.enchants.ArmorEquipListener;
import bigbade.enchantmenttokens.listeners.enchants.BlockBreakListener;
import bigbade.enchantmenttokens.listeners.enchants.BlockDamageListener;
import bigbade.enchantmenttokens.listeners.enchants.EnchantmentListener;
import bigbade.enchantmenttokens.listeners.gui.EnchantmentGUIListener;
import com.codingforcookies.armorequip.ArmorListener;
import com.codingforcookies.armorequip.DispenserArmorListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

public class ListenerHandler {
    private Map<ListenerType, ListenerManager> enchantListeners = new ConcurrentHashMap<>();
    private EnchantmentTokens main;

    public ListenerHandler(EnchantmentTokens main) {
        this.main = main;
    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new ArmorListener(new ArrayList<>()), main);
        Bukkit.getPluginManager().registerEvents(new DispenserArmorListener(), main);

        Bukkit.getPluginManager().registerEvents(new SignPlaceListener(main.getEnchantmentHandler()), main);
        Bukkit.getPluginManager().registerEvents(new SignClickListener(main), main);

        Bukkit.getPluginManager().registerEvents(new EnchantmentGUIListener(main, main.getEnchantmentPickerManager(), main.getVersion()), main);

        Bukkit.getPluginManager().registerEvents(new ChunkUnloadListener(main.getSigns()), main);
        Bukkit.getPluginManager().registerEvents(new PlayerLeaveListener(main), main);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(main.getPlayerHandler()), main);
        
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(enchantListeners.get(ListenerType.BLOCKBREAK), main), main);
        Bukkit.getPluginManager().registerEvents(new ArmorEquipListener(enchantListeners.get(ListenerType.EQUIP), enchantListeners.get(ListenerType.UNEQUIP)), main);
        Bukkit.getPluginManager().registerEvents(new BlockDamageListener(enchantListeners.get(ListenerType.BLOCKDAMAGED)), main);
        Bukkit.getPluginManager().registerEvents(new InventoryMoveListener(enchantListeners.get(ListenerType.HELD), enchantListeners.get(ListenerType.SWAPPED), main), main);
    }

    public void onEnchant(ItemStack item, EnchantmentBase base, Player player) {
        ListenerManager manager = enchantListeners.get(ListenerType.ENCHANT);
        EnchantmentEvent<EnchantmentApplyEvent> enchantmentEvent = new EnchantmentEvent<>(new EnchantmentApplyEvent(item, player), item).setUser(player);
        manager.callEvent(enchantmentEvent, base);
    }

    public void loadAddons(Collection<EnchantmentAddon> addons) {
        for (EnchantmentAddon addon : addons) {
            FileConfiguration configuration = ConfigurationManager.loadConfigurationFile(main.getDataFolder().getAbsolutePath() + "\\enchantments\\" + addon.getName() + ".yml");

            for (Field field : addon.getClass().getDeclaredFields()) {
                ConfigurationManager.loadConfigForField(field, configuration, addon);
            }

            addon.loadConfig(configuration);
            for (Method method : addon.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(EnchantListener.class) && method.getReturnType() == EnchantmentListener.class) {
                    ListenerType type = method.getAnnotation(EnchantListener.class).type();
                    enchantListeners.get(type).add((EnchantmentListener<EnchantmentEvent<? extends Event>>) ReflectionManager.invoke(method, addon), addon);
                }
            }
        }
    }

    public void loadEnchantments(Map<String, Set<Class<EnchantmentBase>>> enchants) {
        ConcurrentLinkedQueue<EnchantmentBase> enchantments = new ConcurrentLinkedQueue<>();

        for (ListenerType type : ListenerType.values()) {
            enchantListeners.put(type, new ListenerManager());
        }

        Map<String, FileConfiguration> configs = new HashMap<>();
        for (Map.Entry<String, Set<Class<EnchantmentBase>>> entry : enchants.entrySet()) {
            for (Class<EnchantmentBase> clazz : entry.getValue()) {
                FileConfiguration configuration = configs.get(entry.getKey());

                if (configuration == null) {
                    configuration = ConfigurationManager.loadConfigurationFile(main.getDataFolder().getAbsolutePath() + "\\enchantments\\" + entry.getKey() + ".yml");
                    configs.put(entry.getKey(), configuration);
                }

                ConfigurationSection section = configuration.getConfigurationSection("enchants");
                if (section == null)
                    section = configuration.createSection("enchants");
                String name = clazz.getSimpleName();
                ConfigurationSection enchantSection = section.getConfigurationSection(name.toLowerCase());
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
                if (enchantSection.isSet("enabled"))
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
                }
            }
        }

        Bukkit.getScheduler().runTask(main, () -> {
            main.getLogger().log(Level.INFO, "Finishing loading enchantments");
            main.getEnchantmentHandler().registerEnchants(enchantments);
        });

        registerListeners();

        for (Map.Entry<String, FileConfiguration> configuration : configs.entrySet()) {
            ConfigurationManager.saveConfiguration(main.getDataFolder().getAbsolutePath() + "\\enchantments\\" + configuration.getKey() + ".yml", configuration.getValue());
        }
    }
}
