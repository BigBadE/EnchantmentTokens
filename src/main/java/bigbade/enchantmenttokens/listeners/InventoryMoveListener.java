package bigbade.enchantmenttokens.listeners;

/*
EnchantmentTokens
Copyright (C) 2019-2020 Big_Bad_E
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

import bigbade.enchantmenttokens.EnchantmentTokens;
import bigbade.enchantmenttokens.events.EnchantmentEvent;
import bigbade.enchantmenttokens.listeners.enchants.BasicEnchantListener;
import bigbade.enchantmenttokens.utils.ListenerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryMoveListener extends BasicEnchantListener<Event> implements Listener {
    private EnchantmentTokens main;

    private ListenerManager swapOn;
    private ListenerManager swapOff;

    public InventoryMoveListener(ListenerManager swapOn, ListenerManager swapOff, EnchantmentTokens main) {
        super(null);
        this.main = main;
        this.swapOn = swapOn;
        this.swapOff = swapOff;
    }

    @EventHandler
    public void onInventorySwap(InventoryClickEvent event) {
        if (event.getInventory().getHolder() == null) return;
        if (event.getInventory().getHolder().equals(event.getWhoClicked()))
            if (event.getSlot() == event.getWhoClicked().getInventory().getHeldItemSlot()) {
                for (Location location : main.signHandler.getSigns())
                    if (location.getChunk().isLoaded() && location.getWorld() == event.getWhoClicked().getWorld())
                        Bukkit.getScheduler().scheduleSyncDelayedTask(main, () ->
                                ((Player) event.getWhoClicked()).sendSignChange(location, new String[]{"[Enchantment]", ((Sign) location.getBlock().getState()).getLine(1), "", ""}));
                if(event.getCurrentItem() != null) {
                    EnchantmentEvent<Event> enchantmentEvent = new EnchantmentEvent<Event>(event, event.getCurrentItem()).setUser(event.getWhoClicked());
                    callListeners(enchantmentEvent, swapOn);
                }

                if(event.getCursor() != null) {
                    EnchantmentEvent<Event> enchantmentEvent = new EnchantmentEvent<Event>(event, event.getCursor()).setUser(event.getWhoClicked());
                    callListeners(enchantmentEvent, swapOff);
                }
            }
    }

    @EventHandler
    public void onHandSwap(PlayerItemHeldEvent event) {
        for (Location location : main.signHandler.getSigns()) {
            if (location.getChunk().isLoaded() && location.getWorld() == event.getPlayer().getWorld()) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(main, () ->
                        event.getPlayer().sendSignChange(location, new String[]{"[Enchantment]", ((Sign) location.getBlock().getState()).getLine(1), "", ""}));
            }
        }

        ItemStack item = event.getPlayer().getInventory().getItem(event.getPreviousSlot());
        if(item != null) {
            EnchantmentEvent<Event> enchantmentEvent = new EnchantmentEvent<Event>(event, item).setUser(event.getPlayer());
            callListeners(enchantmentEvent, swapOff);
        }

        item = event.getPlayer().getInventory().getItem(event.getNewSlot());
        if(item != null) {
            EnchantmentEvent<Event> enchantmentEvent = new EnchantmentEvent<Event>(event, item).setUser(event.getPlayer());
            callListeners(enchantmentEvent, swapOn);
        }
    }
}