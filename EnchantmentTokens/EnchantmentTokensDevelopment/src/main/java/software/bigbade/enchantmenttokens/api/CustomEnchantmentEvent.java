/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.api;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CustomEnchantmentEvent extends Event implements EnchantmentEvent {
    private Entity user;
    private Entity targetEntity;
    private Block targetBlock;
    private ItemStack item;
    private ListenerType type;

    private static HandlerList handlers = new HandlerList();

    public CustomEnchantmentEvent(ListenerType type, ItemStack item) {
        this.type = type;
        this.item = item;
    }

    //Needed for Skript
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Nonnull
    public Entity getUser() {
        return user;
    }

    @Nonnull
    public EnchantmentEvent setUser(Entity user) {
        this.user = user;
        return this;
    }

    @Nullable
    public Entity getTargetEntity() {
        return targetEntity;
    }

    @Nonnull
    public EnchantmentEvent setTargetEntity(Entity targetEntity) {
        this.targetEntity = targetEntity;
        return this;
    }

    public Block getTargetBlock() {
        return targetBlock;
    }

    @Override
    public Event getEvent() {
        return this;
    }

    @Nonnull
    public EnchantmentEvent setTargetBlock(Block targetBlock) {
        this.targetBlock = targetBlock;
        return this;
    }

    @Nonnull
    public ItemStack getItem() {
        return item;
    }

    @Nonnull
    public EnchantmentEvent setItem(ItemStack item) {
        this.item = item;
        return this;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }

    public ListenerType getType() {
        return type;
    }
}
