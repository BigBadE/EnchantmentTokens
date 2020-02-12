package software.bigbade.enchantmenttokens.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EnchantmentEvent extends Event {
    private Entity user;
    private Entity targetEntity;
    private Block targetBlock;
    private ItemStack item;

    private HandlerList handlers;

    public EnchantmentEvent(ItemStack item) {
        this.item = item;
    }

    public Entity getUser() {
        return user;
    }

    public EnchantmentEvent setUser(Entity user) {
        this.user = user;
        return this;
    }

    public Entity getTargetEntity() {
        return targetEntity;
    }

    public EnchantmentEvent setTargetEntity(Entity targetEntity) {
        this.targetEntity = targetEntity;
        return this;
    }

    public Block getTargetBlock() {
        return targetBlock;
    }

    public EnchantmentEvent setTargetBlock(Block targetBlock) {
        this.targetBlock = targetBlock;
        return this;
    }

    public ItemStack getItem() {
        return item;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
