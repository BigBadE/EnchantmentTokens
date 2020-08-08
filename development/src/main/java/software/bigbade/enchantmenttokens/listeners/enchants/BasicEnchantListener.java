/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners.enchants;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

public class BasicEnchantListener<T extends Event> {
    private ListenerManager<T> listener;

    public BasicEnchantListener() {
    }

    public BasicEnchantListener(ListenerManager<T> listeners) {
        this.listener = listeners;
    }

    public void callListeners(EnchantmentEvent<T> event) {
        listener.callEvent(event);
    }

    public void callListeners(EnchantmentEvent<T> event, ListenerManager<T> targetListener) {
        targetListener.callEvent(event);
    }

    public void callForAllItems(ListenerManager<T> listener, EnchantmentEvent<T> event) {
        Player player = event.getUser();
        for (ItemStack item : player.getInventory().getArmorContents()) {
            if (item != null) {
                callListenerForItem(item, listener, event);
            }
        }

        callListenerForItem(player.getInventory().getItemInMainHand(), listener, event);
        callListenerForItem(player.getInventory().getItemInOffHand(), listener, event);
    }

    public void callForAllItems(EnchantmentEvent<T> event) {
        callForAllItems(listener, event);
    }

    public void callListenerForItem(ItemStack item, ListenerManager<T> listener, EnchantmentEvent<T> event) {
        if (item != null && item.getType() != Material.AIR) {
            event.setItem(item);
            callListeners(event, listener);
        }
    }
}
