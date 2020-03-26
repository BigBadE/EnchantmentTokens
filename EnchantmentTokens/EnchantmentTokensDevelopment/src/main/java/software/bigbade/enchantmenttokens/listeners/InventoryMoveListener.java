package software.bigbade.enchantmenttokens.listeners;

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
import software.bigbade.enchantmenttokens.events.EventFactory;
import software.bigbade.enchantmenttokens.listeners.enchants.BasicEnchantListener;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

import java.util.Set;

public class InventoryMoveListener extends BasicEnchantListener implements Listener {
    private Set<Location> signs;
    private SchedulerHandler scheduler;

    private ListenerManager swapOn;
    private ListenerManager swapOff;

    public InventoryMoveListener(ListenerManager swapOn, ListenerManager swapOff, Set<Location> signs, SchedulerHandler scheduler) {
        super(null);
        this.scheduler = scheduler;
        this.signs = signs;
        this.swapOn = swapOn;
        this.swapOff = swapOff;
    }

    @EventHandler
    public void onInventorySwap(InventoryClickEvent event) {
        if (event.getInventory().getHolder() != null && event.getInventory().getHolder().equals(event.getWhoClicked()) && event.getSlot() == event.getWhoClicked().getInventory().getHeldItemSlot()) {
            updateSigns((Player) event.getWhoClicked());
            if (event.getCurrentItem() != null) {
                EnchantmentEvent enchantmentEvent = EventFactory.createEvent(ListenerType.HELD, event.getCurrentItem()).setUser(event.getWhoClicked());
                callListeners(enchantmentEvent, swapOn);
            }

            if (event.getCursor() != null) {
                EnchantmentEvent enchantmentEvent = EventFactory.createEvent(ListenerType.SWAPPED, event.getCursor()).setUser(event.getWhoClicked());
                callListeners(enchantmentEvent, swapOff);
            }
        }
    }

    private void updateSigns(Player player) {
        for (Location location : signs)
            if (location.getWorld() == player.getWorld())
                scheduler.runTaskLater(() ->
                        player.sendSignChange(location, new String[]{"[Enchantment]", ((Sign) location.getBlock().getState()).getLine(1), "", ""}), 0L);
    }

    @EventHandler
    public void onHandSwap(PlayerItemHeldEvent event) {
        for (Location location : signs) {
            if (location.getChunk().isLoaded() && location.getWorld() == event.getPlayer().getWorld()) {
                scheduler.runTaskLater(() ->
                        event.getPlayer().sendSignChange(location, new String[]{"[Enchantment]", ((Sign) location.getBlock().getState()).getLine(1), "", ""}), 0L);
            }
        }

        ItemStack item = event.getPlayer().getInventory().getItem(event.getPreviousSlot());
        if (item != null) {
            EnchantmentEvent enchantmentEvent = EventFactory.createEvent(ListenerType.SWAPPED, item).setUser(event.getPlayer());
            callListeners(enchantmentEvent, swapOff);
        }

        item = event.getPlayer().getInventory().getItem(event.getNewSlot());
        if (item != null) {
            EnchantmentEvent enchantmentEvent = EventFactory.createEvent(ListenerType.HELD, item).setUser(event.getPlayer());
            callListeners(enchantmentEvent, swapOn);
        }
    }
}