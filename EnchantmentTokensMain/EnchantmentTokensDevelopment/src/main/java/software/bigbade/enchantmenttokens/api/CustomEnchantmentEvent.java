/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.api;

import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Getter
public class CustomEnchantmentEvent<T extends Event> extends Event implements EnchantmentEvent<T> {
    private static final HandlerList handlers = new HandlerList();
    @Getter
    @Nonnull
    private final T event;
    @Nonnull
    private final Player user;
    @Nullable
    private Entity targetEntity;
    @Nullable
    private Block targetBlock;
<<<<<<< HEAD:EnchantmentTokens/EnchantmentTokensDevelopment/src/main/java/software/bigbade/enchantmenttokens/api/CustomEnchantmentEvent.java

    private ItemStack item = null;
=======
    private ItemStack item;
    private final ListenerType type;

    private static final HandlerList handlers = new HandlerList();
>>>>>>> 3d705af96ebb617ac55d44878c2077b5e14535b9:EnchantmentTokensMain/EnchantmentTokensDevelopment/src/main/java/software/bigbade/enchantmenttokens/api/CustomEnchantmentEvent.java

    public CustomEnchantmentEvent(@Nonnull T event, @Nullable ItemStack item, @Nonnull Player user) {
        if (item != null) {
            this.item = item;
        }
        this.user = user;
        this.event = event;
    }

    //Needed for Skript
    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Nonnull
    @Override
    public EnchantmentEvent<T> setItem(ItemStack item) {
        this.item = item;
        return this;
    }

    @Nonnull
    @Override
    public EnchantmentEvent<T> setTargetEntity(Entity entity) {
        this.targetEntity = entity;
        return this;
    }

    @Nonnull
    @Override
    public EnchantmentEvent<T> setTargetBlock(Block block) {
        this.targetBlock = block;
        return this;
    }

    @Override
    public Event getSelfEvent() {
        return this;
    }

    @Nonnull
    public HandlerList getHandlers() {
        return getHandlerList();
    }
}
