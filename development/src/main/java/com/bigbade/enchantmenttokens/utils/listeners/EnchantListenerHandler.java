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

package com.bigbade.enchantmenttokens.utils.listeners;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import com.bigbade.enchantmenttokens.EnchantmentTokens;
import com.bigbade.enchantmenttokens.api.EnchantmentBase;
import com.bigbade.enchantmenttokens.api.EventFactory;
import com.bigbade.enchantmenttokens.api.ListenerType;
import com.bigbade.enchantmenttokens.configuration.ConfigurationManager;
import com.bigbade.enchantmenttokens.configuration.ConfigurationType;
import com.bigbade.enchantmenttokens.events.EnchantmentApplyEvent;
import com.bigbade.enchantmenttokens.events.EnchantmentEvent;
import com.bigbade.enchantmenttokens.listeners.ChunkUnloadListener;
import com.bigbade.enchantmenttokens.listeners.EnchantTableListener;
import com.bigbade.enchantmenttokens.listeners.GemFindListener;
import com.bigbade.enchantmenttokens.listeners.InventoryMoveListener;
import com.bigbade.enchantmenttokens.listeners.PlayerJoinListener;
import com.bigbade.enchantmenttokens.listeners.PlayerLeaveListener;
import com.bigbade.enchantmenttokens.listeners.SignClickListener;
import com.bigbade.enchantmenttokens.listeners.SignPlaceListener;
import com.bigbade.enchantmenttokens.listeners.enchants.ArmorEquipListener;
import com.bigbade.enchantmenttokens.listeners.enchants.BlockBreakListener;
import com.bigbade.enchantmenttokens.listeners.enchants.BlockDamageListener;
import com.bigbade.enchantmenttokens.listeners.enchants.PlayerDamageListener;
import com.bigbade.enchantmenttokens.listeners.enchants.PlayerDeathListener;
import com.bigbade.enchantmenttokens.listeners.enchants.PotionListener;
import com.bigbade.enchantmenttokens.listeners.enchants.ProjectileHitListener;
import com.bigbade.enchantmenttokens.listeners.enchants.ProjectileShootListener;
import com.bigbade.enchantmenttokens.listeners.enchants.RiptideListener;
import com.bigbade.enchantmenttokens.listeners.gui.EnchantmentGUIListener;
import com.bigbade.enchantmenttokens.utils.ReflectionManager;
import com.bigbade.enchantmenttokens.utils.currency.VaultCurrencyFactory;

public class EnchantListenerHandler implements ListenerHandler {
    private final TypedListenerHandler enchantListeners = new TypedListenerHandler();
    private final EnchantmentTokens main;
    private ListenerManager<EnchantmentApplyEvent> enchantListener;

    public EnchantListenerHandler(EnchantmentTokens main) {
        for (ListenerType type : ListenerType.values()) {
            enchantListeners.register(type, new ListenerManager<>());
        }
        this.main = main;
    }

    @Override
    public void registerListeners() {
        enchantListener = enchantListeners.getManager(ListenerType.ENCHANT);

        Bukkit.getPluginManager().registerEvents(
                new SignPlaceListener(main.getEnchantmentHandler(), main.getPlayerHandler(),
                        main.getSignHandler()), main);
        Bukkit.getPluginManager().registerEvents(
                new EnchantmentGUIListener(main.getPlayerHandler(), main.getScheduler()), main);

        Bukkit.getPluginManager().registerEvents(
                new SignClickListener(main.getEnchantmentHandler(), main.getPlayerHandler()), main);

        Bukkit.getPluginManager().registerEvents(new ChunkUnloadListener(main.getSignHandler().getSigns()), main);
        Bukkit.getPluginManager().registerEvents(new PlayerLeaveListener(main.getPlayerHandler()), main);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(main.getPlayerHandler()), main);

        if (main.isOverridingEnchantTables()) {
            Bukkit.getPluginManager().registerEvents(
                    new EnchantTableListener(main.getEnchantmentHandler(), main.getPlayerHandler(),
                            ConfigurationManager.getSectionOrCreate(main.getConfig(), "enchantment-table")),
                    main);
        }

        if (ReflectionManager.VERSION >= 13) {
            Bukkit.getPluginManager().registerEvents(
                    new RiptideListener(enchantListeners.getManager(ListenerType.RIPTIDE)), main);
        }

        Bukkit.getPluginManager().registerEvents(
                new PlayerDeathListener(enchantListeners.getManager(ListenerType.DEATH_AFTER)), main);
        Bukkit.getPluginManager().registerEvents(
                new ProjectileHitListener(enchantListeners.getManager(ListenerType.TRIDENT_HIT),
                        enchantListeners.getManager(ListenerType.ARROW_HIT)), main);
        Bukkit.getPluginManager().registerEvents(
                new ProjectileShootListener(main, enchantListeners.getManager(ListenerType.TRIDENT_THROW),
                        enchantListeners.getManager(ListenerType.BOW_SHOOT),
                        enchantListeners.getManager(ListenerType.CROSSBOW_SHOOT)), main);
        Bukkit.getPluginManager().registerEvents(
                new PlayerDamageListener(enchantListeners.getManager(ListenerType.ON_DAMAGED),
                        enchantListeners.getManager(ListenerType.ENTITY_DAMAGED),
                        enchantListeners.getManager(ListenerType.SHIELD_BLOCK),
                        enchantListeners.getManager(ListenerType.SWORD_BLOCK),
                        enchantListeners.getManager(ListenerType.DEATH_BEFORE)), main);
        Bukkit.getPluginManager().registerEvents(
                new PotionListener(enchantListeners.getManager(ListenerType.POTION_APPLY),
                        enchantListeners.getManager(ListenerType.POTION_REMOVE)), main);

        registerGemListener();
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(
                enchantListeners.getManager(ListenerType.BLOCK_BREAK), main.getSignHandler(),
                main.getPlayerHandler()), main);
        Bukkit.getPluginManager().registerEvents(new ArmorEquipListener(
                enchantListeners.getManager(ListenerType.EQUIP),
                enchantListeners.getManager(ListenerType.UNEQUIP)), main);
        Bukkit.getPluginManager().registerEvents(new BlockDamageListener(
                enchantListeners.getManager(ListenerType.BLOCK_DAMAGED)), main);
        Bukkit.getPluginManager().registerEvents(new InventoryMoveListener(
                enchantListeners.getManager(ListenerType.HELD),
                enchantListeners.getManager(ListenerType.SWAPPED),
                main.getSignHandler().getSigns(), main.getScheduler()), main);
    }

    private void registerGemListener() {
        if (!(main.getCurrencyHandler() instanceof VaultCurrencyFactory)) {
            ConfigurationSection section = ConfigurationManager
                    .getSectionOrCreate(main.getConfig(), "currency");
            double chance = new ConfigurationType<>(0.05).getValue("chance", section);
            if (chance > 0) {
                Bukkit.getPluginManager().registerEvents(
                        new GemFindListener(main.getPlayerHandler(), chance, section), main);
            }
        }
    }

    @Override
    public void onEnchant(ItemStack item, EnchantmentBase base, Player player) {
        EnchantmentApplyEvent event = new EnchantmentApplyEvent(item, player);
        EnchantmentEvent<EnchantmentApplyEvent> enchantmentEvent = EventFactory.createEvent(event, item, player);
        enchantListener.callEvent(enchantmentEvent, base.getEnchantment());
    }

    @Override
    public <T extends Event> ListenerManager<T> getListenerManager(ListenerType type) {
        return enchantListeners.getManager(type);
    }
}
