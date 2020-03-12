package software.bigbade.enchantmenttokens.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EnchantmentEvent {
    @NotNull
    EnchantmentEvent setUser(Entity user);
    @NotNull
    Entity getUser();

    @NotNull
    EnchantmentEvent setItem(ItemStack item);
    @NotNull
    ItemStack getItem();

    @NotNull
    EnchantmentEvent setTargetEntity(Entity entity);
    @Nullable
    Entity getTargetEntity();

    @NotNull
    EnchantmentEvent setTargetBlock(Block block);
    @Nullable
    Block getTargetBlock();

    Event getEvent();
}
