package software.bigbade.enchantmenttokens.listeners.gui;

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

import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.gui.EnchantmentGUI;
import software.bigbade.enchantmenttokens.utils.EnchantButton;
import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class EnchantmentGUIListener implements Listener {
    private static String[] c = {"", "C", "CC", "CCC", "CD", "D",
            "DC", "DCC", "DCCC", "CM"};
    private static String[] x = {"", "X", "XX", "XXX", "XL", "L",
            "LX", "LXX", "LXXX", "XC"};
    private static String[] i = {"", "I", "II", "III", "IV", "V",
            "VI", "VII", "VIII", "IX"};

    private EnchantmentPlayerHandler handler;
    private SchedulerHandler scheduler;

    public EnchantmentGUIListener(EnchantmentPlayerHandler handler, SchedulerHandler scheduler) {
        this.handler = handler;
        this.scheduler = scheduler;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        EnchantmentPlayer player = handler.getPlayer((Player) event.getWhoClicked());
        if (event.getClickedInventory() == null || event.getCurrentItem() == null || player.getCurrentGUI() == null)
            return;
        event.setCancelled(true);
        EnchantButton button = player.getCurrentGUI().getButton(event.getSlot());
        if(button == null) return;
        EnchantmentGUI inventory = button.click(event.getInventory().getItem(4));
        if (inventory == null) {
            player.setCurrentGUI(null);
            player.getPlayer().closeInventory();
        } else {
            player.setCurrentGUI(null);
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

    public static String getRomanNumeral(int level) {
        return c[level / 100] +
                x[(level % 100) / 10] +
                i[level % 10];
    }
}
