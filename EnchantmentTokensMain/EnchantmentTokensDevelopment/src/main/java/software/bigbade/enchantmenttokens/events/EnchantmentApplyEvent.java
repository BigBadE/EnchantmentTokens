/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class EnchantmentApplyEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final ItemStack item;
    private final Player player;

    public EnchantmentApplyEvent(ItemStack item, Player player) {
        this.item = item;
        this.player = player;
    }

    public ItemStack getItem() {
        return item;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    @Nonnull
    public HandlerList getHandlers() {
        return handlers;
    }
}
