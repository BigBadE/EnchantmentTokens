package software.bigbade.enchantmenttokens.events;

import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import software.bigbade.enchantmenttokens.api.ListenerType;

public class EnchantmentEvent extends Event {
    private Entity user;
    private Entity targetEntity;
    private Block targetBlock;
    private ItemStack item;
    private ListenerType type;
    private Enchantment enchantment;

    private static HandlerList handlers = new HandlerList();

    public EnchantmentEvent(ListenerType type, ItemStack item) {
        this.type = type;
        this.item = item;
    }

    public static HandlerList getHandlerList() {
        return handlers;
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

    public ListenerType getType() {
        return type;
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public void setEnchantment(Enchantment enchantment) {
        this.enchantment = enchantment;
    }
}
