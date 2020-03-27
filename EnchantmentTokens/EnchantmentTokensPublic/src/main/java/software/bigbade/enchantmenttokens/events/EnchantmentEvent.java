package software.bigbade.enchantmenttokens.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface EnchantmentEvent {
    @Nonnull
    EnchantmentEvent setUser(Entity user);
    @Nonnull
    Entity getUser();

    @Nonnull
    EnchantmentEvent setItem(ItemStack item);
    @Nonnull
    ItemStack getItem();

    @Nonnull
    EnchantmentEvent setTargetEntity(Entity entity);
    @Nullable
    Entity getTargetEntity();

    @Nonnull
    EnchantmentEvent setTargetBlock(Block block);
    @Nullable
    Block getTargetBlock();

    Event getEvent();
}
