package bigbade.enchantmenttokens.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class EnchantmentApplyEvent extends Event {
    private ItemStack item;
    private Player player;

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
    public HandlerList getHandlers() {
        return null;
    }
}
