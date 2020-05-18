/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.api.EventFactory;
import software.bigbade.enchantmenttokens.api.ListenerType;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.listeners.enchants.BasicEnchantListener;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

import java.util.Set;

public class InventoryMoveListener extends BasicEnchantListener implements Listener {
    private final Set<Location> signs;
    private final SchedulerHandler scheduler;

    private final ListenerManager<?> swapOn;
    private final ListenerManager<?> swapOff;

    public InventoryMoveListener(ListenerManager<?> swapOn, ListenerManager<?> swapOff, Set<Location> signs, SchedulerHandler scheduler) {
        super(null);
        this.scheduler = scheduler;
        this.signs = signs;
        this.swapOn = swapOn;
        this.swapOff = swapOff;
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        EnchantmentEvent<PlayerDropItemEvent> enchantmentEvent = new EventFactory<PlayerDropItemEvent>().createEvent(event, ListenerType.HELD, event.getItemDrop().getItemStack(), event.getPlayer());
        callListeners(enchantmentEvent, swapOff);
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            //Seems that the item instance is copied/isn't destroyed in one tick so this should work.
            scheduler.runTaskLater(() -> {
                Player player = (Player) event.getEntity();
                if (player.getInventory().getItemInMainHand().equals(event.getItem().getItemStack())) {
                    EnchantmentEvent<EntityPickupItemEvent> enchantmentEvent = new EventFactory<EntityPickupItemEvent>().createEvent(event, ListenerType.HELD, player.getInventory().getItemInMainHand(), player);
                    callListeners(enchantmentEvent, swapOn);
                }
            }, 0L);
        }
    }

    @EventHandler
    public void onInventorySwap(InventoryClickEvent event) {
        if (event.getInventory().getHolder() != null && event.getInventory().getHolder().equals(event.getWhoClicked()) && event.getSlot() == event.getWhoClicked().getInventory().getHeldItemSlot()) {
            Player player = (Player) event.getWhoClicked();
            updateSigns(player);
            if (event.getCurrentItem() != null) {
                EnchantmentEvent<InventoryClickEvent> enchantmentEvent = new EventFactory<InventoryClickEvent>().createEvent(event, ListenerType.HELD, event.getCurrentItem(), event.getWhoClicked());
                callListeners(enchantmentEvent, swapOff);
            }

            if (event.getCursor() != null && event.getSlot() == player.getInventory().getHeldItemSlot()) {
                EnchantmentEvent<InventoryClickEvent> enchantmentEvent = new EventFactory<InventoryClickEvent>().createEvent(event, ListenerType.SWAPPED, event.getCursor(), event.getWhoClicked());
                callListeners(enchantmentEvent, swapOn);
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
            EnchantmentEvent<PlayerItemHeldEvent> enchantmentEvent = new EventFactory<PlayerItemHeldEvent>().createEvent(event, ListenerType.SWAPPED, item, event.getPlayer());
            callListeners(enchantmentEvent, swapOff);
        }

        item = event.getPlayer().getInventory().getItem(event.getNewSlot());
        if (item != null) {
            EnchantmentEvent<PlayerItemHeldEvent> enchantmentEvent = new EventFactory<PlayerItemHeldEvent>().createEvent(event, ListenerType.HELD, item, event.getPlayer());
            callListeners(enchantmentEvent, swapOn);
        }
    }
}