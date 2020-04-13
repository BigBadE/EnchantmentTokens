/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.gui.EnchantButton;
import software.bigbade.enchantmenttokens.gui.EnchantmentGUI;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;
import software.bigbade.enchantmenttokens.utils.players.PlayerHandler;

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
        if (event.getCurrentItem() == null || player.getCurrentGUI() == null)
            return;
        event.setCancelled(true);
        EnchantButton button = player.getCurrentGUI().getButton(event.getSlot());
        if(button == null) return;
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
        if (enchantPlayer.getCurrentGUI() != null && enchantPlayer.getCurrentGUI().getInventory() != null && enchantPlayer.getCurrentGUI().getInventory().equals(event.getInventory())) {
            scheduler.runTaskLater(() -> {
                EnchantmentGUI gui = enchantPlayer.getCurrentGUI();
                enchantPlayer.setCurrentGUI(null);
                event.getPlayer().openInventory(event.getInventory());
                enchantPlayer.setCurrentGUI(gui);
            }, 1L);
        }
    }
}
