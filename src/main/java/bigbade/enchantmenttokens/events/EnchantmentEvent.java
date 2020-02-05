package bigbade.enchantmenttokens.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class EnchantmentEvent<T extends Event> {
    private T event;
    private Entity user;
    private Entity targetEntity;
    private Block targetBlock;
    private ItemStack item;

    public EnchantmentEvent(T event, ItemStack item) {
        this.event = event;
        this.item = item;
    }

    public T getEvent() {
        return event;
    }

    public EnchantmentEvent<T> setEvent(T event) {
        this.event = event;
        return this;
    }

    public Entity getUser() {
        return user;
    }

    public EnchantmentEvent<T> setUser(Entity user) {
        this.user = user;
        return this;
    }

    public Entity getTargetEntity() {
        return targetEntity;
    }

    public EnchantmentEvent<T> setTargetEntity(Entity targetEntity) {
        this.targetEntity = targetEntity;
        return this;
    }

    public Block getTargetBlock() {
        return targetBlock;
    }

    public EnchantmentEvent<T> setTargetBlock(Block targetBlock) {
        this.targetBlock = targetBlock;
        return this;
    }

    public ItemStack getItem() {
        return item;
    }
}
