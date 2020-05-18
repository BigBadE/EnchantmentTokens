/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners.enchants;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

public class BasicEnchantListener {
    private ListenerManager<?> listeners;

    public BasicEnchantListener() {}

    public BasicEnchantListener(ListenerManager<?> listeners) {
        this.listeners = listeners;
    }

    public void callListeners(EnchantmentEvent<?> event) {
        listeners.callEvent(event);
        Bukkit.getPluginManager().callEvent(event.getSelfEvent());
    }

    public void callListeners(EnchantmentEvent<?> event, ListenerManager<?> listeners) {
        listeners.callEvent(event);
        Bukkit.getPluginManager().callEvent(event.getSelfEvent());
    }

    public void callForAllItems(Player player, ListenerManager<?> listener, EnchantmentEvent<?> event) {
        for (ItemStack item : player.getInventory().getArmorContents()) {
            callListenerForItem(item, listener, event);
        }

        callListenerForItem(player.getInventory().getItemInMainHand(), listener, event);
        callListenerForItem(player.getInventory().getItemInOffHand(), listener, event);
    }

    public void callForAllItems(Player player, EnchantmentEvent<?> event) {
        callForAllItems(player, listeners, event);
    }

    public void callListenerForItem(ItemStack item, ListenerManager<?> listener, EnchantmentEvent<?> event) {
        if (item != null && item.getType() != Material.AIR) {
            event.setItem(item);
            callListeners(event, listener);
        }
    }
}
