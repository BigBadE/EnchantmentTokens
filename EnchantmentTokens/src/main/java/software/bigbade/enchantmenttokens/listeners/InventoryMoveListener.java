package software.bigbade.enchantmenttokens.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.api.ListenerType;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.listeners.enchants.BasicEnchantListener;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

import java.util.List;

public class InventoryMoveListener extends BasicEnchantListener implements Listener {
    private List<Location> signs;
    private SchedulerHandler scheduler;

    private ListenerManager swapOn;
    private ListenerManager swapOff;

    public InventoryMoveListener(ListenerManager swapOn, ListenerManager swapOff, List<Location> signs, SchedulerHandler scheduler) {
        super(null);
        this.scheduler = scheduler;
        this.signs = signs;
        this.swapOn = swapOn;
        this.swapOff = swapOff;
    }

    @EventHandler
    public void onInventorySwap(InventoryClickEvent event) {
        if (event.getInventory().getHolder() == null) return;
        if (event.getInventory().getHolder().equals(event.getWhoClicked()))
            if (event.getSlot() == event.getWhoClicked().getInventory().getHeldItemSlot()) {
                for (Location location : signs)
                    if (location.getChunk().isLoaded() && location.getWorld() == event.getWhoClicked().getWorld())
                        scheduler.runTaskLater(() ->
                                ((Player) event.getWhoClicked()).sendSignChange(location, new String[]{"[Enchantment]", ((Sign) location.getBlock().getState()).getLine(1), "", ""}), 1L);
                if(event.getCurrentItem() != null) {
                    EnchantmentEvent enchantmentEvent = new EnchantmentEvent(ListenerType.HELD, event.getCurrentItem()).setUser(event.getWhoClicked());
                    callListeners(enchantmentEvent, swapOn);
                }

                if(event.getCursor() != null) {
                    EnchantmentEvent enchantmentEvent = new EnchantmentEvent(ListenerType.SWAPPED, event.getCursor()).setUser(event.getWhoClicked());
                    callListeners(enchantmentEvent, swapOff);
                }
            }
    }

    @EventHandler
    public void onHandSwap(PlayerItemHeldEvent event) {
        for (Location location : signs) {
            if (location.getChunk().isLoaded() && location.getWorld() == event.getPlayer().getWorld()) {
                scheduler.runTaskLater(() ->
                        event.getPlayer().sendSignChange(location, new String[]{"[Enchantment]", ((Sign) location.getBlock().getState()).getLine(1), "", ""}), 1L);
            }
        }

        ItemStack item = event.getPlayer().getInventory().getItem(event.getPreviousSlot());
        if(item != null) {
            EnchantmentEvent enchantmentEvent = new EnchantmentEvent(ListenerType.SWAPPED, item).setUser(event.getPlayer());
            callListeners(enchantmentEvent, swapOff);
        }

        item = event.getPlayer().getInventory().getItem(event.getNewSlot());
        if(item != null) {
            EnchantmentEvent enchantmentEvent = new EnchantmentEvent(ListenerType.HELD, item).setUser(event.getPlayer());
            callListeners(enchantmentEvent, swapOn);
        }
    }
}