/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.EnchantingInventory;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.api.VanillaEnchant;
import software.bigbade.enchantmenttokens.configuration.ConfigurationType;
import software.bigbade.enchantmenttokens.listeners.packet.EnchantmentTablePacketHandler;
import software.bigbade.enchantmenttokens.utils.EnchantPickerUtils;
import software.bigbade.enchantmenttokens.utils.ReflectionManager;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantUtils;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import software.bigbade.enchantmenttokens.utils.players.PlayerHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@RequiredArgsConstructor
public class EnchantTableListener implements Listener {
    private final PlayerHandler playerHandler;
    private final List<EnchantmentBase> enchantments = new ArrayList<>();
    private final int maxLevel;

    public EnchantTableListener(EnchantmentHandler enchantmentHandler, PlayerHandler playerHandler, ConfigurationSection section) {
        this.playerHandler = playerHandler;
        enchantments.addAll(enchantmentHandler.getAllEnchants());
        maxLevel = new ConfigurationType<>(30).getValue("max-level", section);
        if (new ConfigurationType<>(true).getValue("include-vanilla-enchants", section)) {
            for (Enchantment enchantment : Enchantment.values()) {
                if (!(enchantment instanceof EnchantmentBase) && !enchantmentHandler.hasVanillaEnchant(enchantment)) {
                    enchantments.add(new VanillaEnchant(enchantment, true));
                }
            }
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEnchantPrepare(PrepareItemEnchantEvent event) {
        long seed = EnchantmentTablePacketHandler.getSeed(event.getEnchanter());
        //Prevent accidentally getting custom enchants, then override it later.
        for (int i = 0; i < 3; i++) {
            int xp = EnchantPickerUtils.getRequiredExperience(new Random(seed + i), i, event.getEnchantmentBonus(), maxLevel, event.getItem());
            event.getOffers()[i] = new EnchantmentOffer(Enchantment.VANISHING_CURSE, 1, xp);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTableEnchant(EnchantItemEvent event) {
        event.setCancelled(true);
        long seed = EnchantmentTablePacketHandler.getSeed(event.getEnchanter());
        seed += event.whichButton();
        //Cycle 6 seeds to simulate the xp being generated.
        seed = (seed * 76790647859193L + 25707281917278L) % 281474976710656L;

        Random random = new Random(seed);
        Map<EnchantmentBase, Integer> possible = EnchantPickerUtils.rollEnchantments(enchantments, random, event.getItem(), event.getExpLevelCost(), false);
        event.getEnchantsToAdd().clear();
        EnchantmentPlayer player = playerHandler.getPlayer(event.getEnchanter());
        for (Map.Entry<EnchantmentBase, Integer> entry : possible.entrySet()) {
            EnchantUtils.getInstance().addEnchantmentBase(event.getItem(), entry.getKey(), player.getPlayer(), entry.getValue());
        }
        EnchantmentTablePacketHandler.onEnchant(event.getEnchanter(), event.getItem(), event.whichButton() + 1);
        EnchantingInventory inventory = (EnchantingInventory) event.getEnchanter().getOpenInventory().getTopInventory();
        if (ReflectionManager.VERSION >= 8 && event.getEnchanter().getGameMode() != GameMode.CREATIVE) {
            Objects.requireNonNull(inventory.getSecondary()).setAmount(inventory.getSecondary().getAmount() - event.whichButton());
        }
    }
}
