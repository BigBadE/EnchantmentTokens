/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.api.EventFactory;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.listeners.enchants.BasicEnchantListener;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

import java.util.Set;

public class InventoryMoveListener extends BasicEnchantListener<Event> implements Listener {
    private final Set<Location> signs;
    private final SchedulerHandler scheduler;

    private final ListenerManager<Event> swapOn;
    private final ListenerManager<Event> swapOff;

    public InventoryMoveListener(ListenerManager<Event> swapOn, ListenerManager<Event> swapOff, Set<Location> signs, SchedulerHandler scheduler) {
        super(null);
        this.scheduler = scheduler;
        this.signs = signs;
        this.swapOn = swapOn;
        this.swapOff = swapOff;
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        EnchantmentEvent<Event> enchantmentEvent = EventFactory.createEvent(event, event.getItemDrop().getItemStack(), event.getPlayer());
        callListeners(enchantmentEvent, swapOff);
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            //Seems that the item instance is copied/isn't destroyed in one tick so this should work.
            scheduler.runTaskLater(() -> {
                Player player = (Player) event.getEntity();
                if (player.getInventory().getItemInMainHand().equals(event.getItem().getItemStack())) {
                    EnchantmentEvent<Event> enchantmentEvent = EventFactory.createEvent(event, player.getInventory().getItemInMainHand(), player);
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
                EnchantmentEvent<Event> enchantmentEvent = EventFactory.createEvent(event, event.getCurrentItem(), (Player) event.getWhoClicked());
                callListeners(enchantmentEvent, swapOff);
            }

            if (event.getCursor() != null && event.getSlot() == player.getInventory().getHeldItemSlot()) {
                EnchantmentEvent<Event> enchantmentEvent = EventFactory.createEvent(event, event.getCursor(), (Player) event.getWhoClicked());
                callListeners(enchantmentEvent, swapOn);
            }
        }
    }

    private void updateSigns(Player player) {
        for (Location location : signs) {
            if (location.getWorld() == player.getWorld()) {
                scheduler.runTaskLater(() -> {
                    Sign sign = (Sign) location.getBlock().getState();
                    player.sendSignChange(location, new String[]{sign.getLine(0), sign.getLine(1), "", ""});
                }, 0L);
            }
        }
    }

    @EventHandler
    public void onHandSwap(PlayerItemHeldEvent event) {
        updateSigns(event.getPlayer());

        ItemStack item = event.getPlayer().getInventory().getItem(event.getPreviousSlot());
        if (item != null) {
            EnchantmentEvent<Event> enchantmentEvent = EventFactory.createEvent(event, item, event.getPlayer());
            callListeners(enchantmentEvent, swapOff);
        }

        item = event.getPlayer().getInventory().getItem(event.getNewSlot());
        if (item != null) {
            EnchantmentEvent<Event> enchantmentEvent = EventFactory.createEvent(event, item, event.getPlayer());
            callListeners(enchantmentEvent, swapOn);
        }
    }
}