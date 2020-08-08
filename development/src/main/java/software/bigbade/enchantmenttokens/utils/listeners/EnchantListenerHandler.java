/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.utils.listeners;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.EventFactory;
import software.bigbade.enchantmenttokens.api.ListenerType;
import software.bigbade.enchantmenttokens.configuration.ConfigurationManager;
import software.bigbade.enchantmenttokens.configuration.ConfigurationType;
import software.bigbade.enchantmenttokens.events.EnchantmentApplyEvent;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.listeners.ChunkUnloadListener;
import software.bigbade.enchantmenttokens.listeners.EnchantTableListener;
import software.bigbade.enchantmenttokens.listeners.GemFindListener;
import software.bigbade.enchantmenttokens.listeners.InventoryMoveListener;
import software.bigbade.enchantmenttokens.listeners.PlayerJoinListener;
import software.bigbade.enchantmenttokens.listeners.PlayerLeaveListener;
import software.bigbade.enchantmenttokens.listeners.SignClickListener;
import software.bigbade.enchantmenttokens.listeners.SignPlaceListener;
import software.bigbade.enchantmenttokens.listeners.enchants.ArmorEquipListener;
import software.bigbade.enchantmenttokens.listeners.enchants.BlockBreakListener;
import software.bigbade.enchantmenttokens.listeners.enchants.BlockDamageListener;
import software.bigbade.enchantmenttokens.listeners.enchants.PlayerDamageListener;
import software.bigbade.enchantmenttokens.listeners.enchants.PlayerDeathListener;
import software.bigbade.enchantmenttokens.listeners.enchants.PotionListener;
import software.bigbade.enchantmenttokens.listeners.enchants.ProjectileHitListener;
import software.bigbade.enchantmenttokens.listeners.enchants.ProjectileShootListener;
import software.bigbade.enchantmenttokens.listeners.enchants.RiptideListener;
import software.bigbade.enchantmenttokens.listeners.gui.EnchantmentGUIListener;
import software.bigbade.enchantmenttokens.utils.ReflectionManager;
import software.bigbade.enchantmenttokens.utils.currency.VaultCurrencyFactory;

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

        Bukkit.getPluginManager().registerEvents(new SignPlaceListener(main.getEnchantmentHandler(), main.getPlayerHandler(), main.getSignHandler()), main);
        Bukkit.getPluginManager().registerEvents(new SignClickListener(main.getUtils()), main);

        Bukkit.getPluginManager().registerEvents(new EnchantmentGUIListener(main.getPlayerHandler(), main.getScheduler()), main);

        Bukkit.getPluginManager().registerEvents(new ChunkUnloadListener(main.getSignHandler().getSigns()), main);
        Bukkit.getPluginManager().registerEvents(new PlayerLeaveListener(main.getPlayerHandler()), main);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(main.getPlayerHandler()), main);

        if (main.isOverridingEnchantTables()) {
            Bukkit.getPluginManager().registerEvents(new EnchantTableListener(main.getEnchantmentHandler(), main.getPlayerHandler(), ConfigurationManager.getSectionOrCreate(main.getConfig(), "enchantment-table")), main);
        }

        if (ReflectionManager.VERSION >= 13) {
            Bukkit.getPluginManager().registerEvents(new RiptideListener(enchantListeners.getManager(ListenerType.RIPTIDE)), main);
        }

        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(enchantListeners.getManager(ListenerType.DEATH_AFTER)), main);
        Bukkit.getPluginManager().registerEvents(new ProjectileHitListener(enchantListeners.getManager(ListenerType.TRIDENT_HIT), enchantListeners.getManager(ListenerType.ARROW_HIT)), main);
        Bukkit.getPluginManager().registerEvents(new ProjectileShootListener(main, enchantListeners.getManager(ListenerType.TRIDENT_THROW), enchantListeners.getManager(ListenerType.BOW_SHOOT), enchantListeners.getManager(ListenerType.CROSSBOW_SHOOT)), main);
        Bukkit.getPluginManager().registerEvents(new PlayerDamageListener(enchantListeners.getManager(ListenerType.ON_DAMAGED), enchantListeners.getManager(ListenerType.ENTITY_DAMAGED), enchantListeners.getManager(ListenerType.SHIELD_BLOCK), enchantListeners.getManager(ListenerType.SWORD_BLOCK), enchantListeners.getManager(ListenerType.DEATH_BEFORE)), main);
        Bukkit.getPluginManager().registerEvents(new PotionListener(enchantListeners.getManager(ListenerType.POTION_APPLY), enchantListeners.getManager(ListenerType.POTION_REMOVE)), main);

        registerGemListener();
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(enchantListeners.getManager(ListenerType.BLOCK_BREAK), main.getSignHandler()), main);
        Bukkit.getPluginManager().registerEvents(new ArmorEquipListener(enchantListeners.getManager(ListenerType.EQUIP), enchantListeners.getManager(ListenerType.UNEQUIP)), main);
        Bukkit.getPluginManager().registerEvents(new BlockDamageListener(enchantListeners.getManager(ListenerType.BLOCK_DAMAGED)), main);
        Bukkit.getPluginManager().registerEvents(new InventoryMoveListener(enchantListeners.getManager(ListenerType.HELD), enchantListeners.getManager(ListenerType.SWAPPED), main.getSignHandler().getSigns(), main.getScheduler()), main);
    }

    private void registerGemListener() {
        if (!(main.getCurrencyHandler() instanceof VaultCurrencyFactory)) {
            ConfigurationSection section = ConfigurationManager.getSectionOrCreate(main.getConfig(), "currency");
            double chance = new ConfigurationType<>(0.05).getValue("chance", section);
            if (chance > 0) {
                Bukkit.getPluginManager().registerEvents(new GemFindListener(main.getPlayerHandler(), chance, section), main);
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
