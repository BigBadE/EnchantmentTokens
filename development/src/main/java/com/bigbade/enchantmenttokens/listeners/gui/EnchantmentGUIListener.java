/*
 * Custom enchantments for Minecraft
 * Copyright (C) 2021 Big_Bad_E
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bigbade.enchantmenttokens.listeners.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import com.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import com.bigbade.enchantmenttokens.gui.EnchantButton;
import com.bigbade.enchantmenttokens.gui.EnchantmentGUI;
import com.bigbade.enchantmenttokens.utils.SchedulerHandler;
import com.bigbade.enchantmenttokens.utils.players.PlayerHandler;

public class EnchantmentGUIListener implements Listener {
    private final PlayerHandler handler;
    private final SchedulerHandler scheduler;

    public EnchantmentGUIListener(PlayerHandler handler, SchedulerHandler scheduler) {
        this.handler = handler;
        this.scheduler = scheduler;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        EnchantmentPlayer player = handler.getPlayer((Player) event.getWhoClicked());
        if (event.getCurrentItem() == null || player.getCurrentGUI() == null) {
            return;
        }
        event.setCancelled(true);
        EnchantButton button = player.getCurrentGUI().getButton(event.getSlot());
        if (button == null) {
            return;
        }
        EnchantmentGUI inventory = button.click(player);
        player.setCurrentGUI(null);
        if (inventory == null) {
            player.getPlayer().closeInventory();
        } else {
            player.getPlayer().openInventory(inventory.getInventory());
            player.setCurrentGUI(inventory);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        EnchantmentPlayer enchantPlayer = handler.getPlayer((Player) event.getPlayer());
        if (enchantPlayer.getCurrentGUI() != null && enchantPlayer.getCurrentGUI().getInventory() != null
                && enchantPlayer.getCurrentGUI().getInventory().equals(event.getInventory())) {
            scheduler.runTaskLater(() -> {
                EnchantmentGUI gui = enchantPlayer.getCurrentGUI();
                enchantPlayer.setCurrentGUI(null);
                event.getPlayer().openInventory(event.getInventory());
                enchantPlayer.setCurrentGUI(gui);
            }, 1L);
        }
    }
}
