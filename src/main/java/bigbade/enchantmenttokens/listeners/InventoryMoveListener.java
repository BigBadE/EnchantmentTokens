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
import bigbade.enchantmenttokens.api.EnchantmentBase;
import bigbade.enchantmenttokens.listeners.enchants.BasicEnchantListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Level;

public class InventoryMoveListener extends BasicEnchantListener implements Listener {
    private EnchantmentTokens main;

    private Map<EnchantmentBase, Method> swapOn;
    private Map<EnchantmentBase, Method> swapOff;

    public InventoryMoveListener(Map<EnchantmentBase, Method> swapOn, Map<EnchantmentBase, Method> swapOff, EnchantmentTokens main) {
        super(null);
        this.main = main;
        this.swapOn = swapOn;
        this.swapOff = swapOff;
    }

    @EventHandler
    public void onInventorySwap(InventoryClickEvent event) {
        if (event.getInventory().getHolder() == null) return;
        if (event.getInventory().getHolder().equals(event.getWhoClicked())) {
            if (event.getSlot() == event.getWhoClicked().getInventory().getHeldItemSlot()) {
                for (Location location : main.signs) {
                    if (location.getChunk().isLoaded() && location.getWorld() == event.getWhoClicked().getWorld()) {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(main, () ->
                                ((Player) event.getWhoClicked()).sendSignChange(location, new String[]{"[Enchantment]", ((Sign) location.getBlock().getState()).getLine(1), "", ""}));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onHandSwap(PlayerItemHeldEvent event) {
        for (Location location : main.signs) {
            if (location.getChunk().isLoaded() && location.getWorld() == event.getPlayer().getWorld()) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(main, () ->
                        event.getPlayer().sendSignChange(location, new String[]{"[Enchantment]", ((Sign) location.getBlock().getState()).getLine(1), "", ""}));
                for (Map.Entry<EnchantmentBase, Method> enchantment : swapOn.entrySet()) {
                    if (event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(enchantment.getKey())) {
                        try {
                            enchantment.getValue().invoke(enchantment.getKey(), event);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            if (e instanceof IllegalAccessException) {
                                EnchantmentTokens.LOGGER.log(Level.SEVERE, "Did not have permission " + enchantment.getValue().getName() + " for enchantment " + enchantment.getKey().name + ", make sure it isn't private/protected", e
                                );
                            } else {
                                EnchantmentTokens.LOGGER.log(Level.SEVERE, "Could not invoke " + enchantment.getValue().getName() + ", check arguments.", e);
                            }
                        }
                    }
                }
                for (Map.Entry<EnchantmentBase, Method> enchantment : swapOff.entrySet()) {
                    if (event.getPlayer().getInventory().getItem(event.getPreviousSlot()).containsEnchantment(enchantment.getKey())) {
                        try {
                            enchantment.getValue().invoke(enchantment.getKey(), event);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            if (e instanceof IllegalAccessException) {
                                EnchantmentTokens.LOGGER.log(Level.SEVERE, "Did not have permission " + enchantment.getValue().getName() + " for enchantment " + enchantment.getKey().name + ", make sure it isn't private/protected", e
                                );
                            } else {
                                EnchantmentTokens.LOGGER.log(Level.SEVERE, "Could not invoke " + enchantment.getValue().getName() + ", check arguments.", e);
                            }
                        }
                    }
                }
            }
        }
    }
}