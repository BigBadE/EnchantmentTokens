package software.bigbade.enchantmenttokens.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bigbade.enchantmenttokens.api.ListenerType;

public class CustomEnchantEvent extends Event implements EnchantmentEvent {
    private Entity user;
    private Entity targetEntity;
    private Block targetBlock;
    private ItemStack item;
    private ListenerType type;

    private static HandlerList handlers = new HandlerList();

    public CustomEnchantEvent(ListenerType type, ItemStack item) {
        this.type = type;
        this.item = item;
    }

    //Needed for Skript
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @NotNull
    public Entity getUser() {
        return user;
    }

    @NotNull
    public EnchantmentEvent setUser(Entity user) {
        this.user = user;
        return this;
    }

    @Nullable
    public Entity getTargetEntity() {
        return targetEntity;
    }

    @NotNull
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

    @NotNull
    public EnchantmentEvent setTargetBlock(Block targetBlock) {
        this.targetBlock = targetBlock;
        return this;
    }

    @NotNull
    public ItemStack getItem() {
        return item;
    }

    @NotNull
    public EnchantmentEvent setItem(ItemStack item) {
        this.item = item;
        return this;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }

    public ListenerType getType() {
        return type;
    }
}
