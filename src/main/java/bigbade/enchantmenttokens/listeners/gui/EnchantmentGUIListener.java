package bigbade.enchantmenttokens.listeners.gui;

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
import bigbade.enchantmenttokens.api.EnchantUtils;
import bigbade.enchantmenttokens.api.EnchantmentPlayer;
import bigbade.enchantmenttokens.api.SubInventory;
import bigbade.enchantmenttokens.commands.EnchantMenuCmd;
import bigbade.enchantmenttokens.gui.EnchantGUI;
import bigbade.enchantmenttokens.gui.EnchantmentGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class EnchantmentGUIListener implements Listener {
    private EnchantGUI enchantGUI;
    private EnchantmentGUI gui = new EnchantmentGUI();
    private Map<Player, SubInventory> subInventories = new HashMap<>();
    private Set<Player> removing = new HashSet<>();
    private EnchantMenuCmd cmd;
    private EnchantmentTokens main;

    private static String[] c = {"", "C", "CC", "CCC", "CD", "D",
            "DC", "DCC", "DCCC", "CM"};
    private static String[] x = {"", "X", "XX", "XXX", "XL", "L",
            "LX", "LXX", "LXXX", "XC"};
    private static String[] i = {"", "I", "II", "III", "IV", "V",
            "VI", "VII", "VIII", "IX"};

    public EnchantmentGUIListener(EnchantmentTokens main, EnchantGUI enchantGUI, EnchantMenuCmd cmd) {
        this.enchantGUI = enchantGUI;
        this.cmd = cmd;
        this.main = main;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        try {
            if (event.getInventory().equals(cmd.inventories.get(event.getWhoClicked()))) {
                event.setCancelled(true);
                if (event.getInventory().getSize() == 45) {
                    Inventory inventory = gui.genInvntory(event.getCurrentItem());
                    cmd.inventories.put((Player) event.getWhoClicked(), inventory);
                    event.getWhoClicked().openInventory(inventory);
                } else {
                    SubInventory inventory = null;
                    switch (event.getSlot()) {
                        case 10:
                            if(EnchantmentTarget.CROSSBOW.includes(event.getClickedInventory().getItem(4).getType())) {
                                inventory = new SubInventory(enchantGUI.generateGUI(EnchantmentTarget.CROSSBOW, event.getClickedInventory().getItem(4), "crossbow"), EnchantmentTarget.CROSSBOW);
                            }
                            break;
                        case 11:
                            if (EnchantmentTarget.TOOL.includes(event.getClickedInventory().getItem(4).getType())) {
                                inventory = new SubInventory(enchantGUI.generateGUI(EnchantmentTarget.TOOL, event.getClickedInventory().getItem(4), "pickaxes"), EnchantmentTarget.TOOL);
                            }
                                break;
                        case 12:
                            if (EnchantmentTarget.WEAPON.includes(event.getClickedInventory().getItem(4).getType()))
                                inventory = new SubInventory(enchantGUI.generateGUI(EnchantmentTarget.WEAPON, event.getClickedInventory().getItem(4), "swords"), EnchantmentTarget.WEAPON);
                            break;
                        case 13:
                            if(EnchantmentTarget.FISHING_ROD.includes(event.getClickedInventory().getItem(4).getType())) {
                                inventory = new SubInventory(enchantGUI.generateGUI(EnchantmentTarget.FISHING_ROD, event.getClickedInventory().getItem(4), "crossbow"), EnchantmentTarget.FISHING_ROD);
                            }
                            break;
                        case 14:
                            if (EnchantmentTarget.ARMOR.includes(event.getClickedInventory().getItem(4).getType()))
                                inventory = new SubInventory(enchantGUI.generateGUI(EnchantmentTarget.ARMOR, event.getClickedInventory().getItem(4), "armor"), EnchantmentTarget.ARMOR);
                            break;
                        case 15:
                            if (EnchantmentTarget.BOW.includes(event.getClickedInventory().getItem(4).getType()))
                                inventory = new SubInventory(enchantGUI.generateGUI(EnchantmentTarget.BOW, event.getClickedInventory().getItem(4), "bows"), EnchantmentTarget.BOW);
                            break;
                        case 16:
                            if(EnchantmentTarget.TRIDENT.includes(event.getClickedInventory().getItem(4).getType())) {
                                inventory = new SubInventory(enchantGUI.generateGUI(EnchantmentTarget.TRIDENT, event.getClickedInventory().getItem(4), "crossbow"), EnchantmentTarget.TRIDENT);
                            }
                            break;
                        case 21:
                            ItemStack adding = event.getInventory().getItem(4);
                            ItemMeta meta = adding.getItemMeta();
                            String line = meta.getLore().get(meta.getLore().size() - 1);
                            long price = Long.parseLong(line.substring(9).replace("G", ""));
                            List<String> lore = meta.getLore();
                            lore.remove(meta.getLore().size() - 1);
                            meta.setLore(lore);
                            EnchantmentPlayer player = main.fileLoader.loadPlayer((Player) event.getWhoClicked());

                            adding.setItemMeta(meta);
                            player.addGems(-price);
                            player.getPlayer().getInventory().setItemInMainHand(adding);
                            removing.add((Player) event.getWhoClicked());
                            event.getWhoClicked().closeInventory();
                            break;
                        case 23:
                            removing.add((Player) event.getWhoClicked());
                            event.getWhoClicked().closeInventory();
                    }
                    if (inventory != null) {
                        removing.add((Player) event.getWhoClicked());
                        event.getWhoClicked().openInventory(inventory.getInventory());
                        subInventories.put((Player) event.getWhoClicked(), inventory);
                    }
                }
            } else if (event.getInventory().equals(subInventories.get(event.getWhoClicked()).getInventory())) {
                event.setCancelled(true);
                ItemStack clicked = event.getCurrentItem();
                if (clicked.getType() == Material.BARRIER) {
                    Inventory inventory = cmd.inventories.get(event.getWhoClicked());
                    inventory.setItem(4, event.getClickedInventory().getItem(4));
                    removing.add((Player) event.getWhoClicked());
                    subInventories.remove(event.getWhoClicked());
                    event.getWhoClicked().openInventory(inventory);
                } else {
                    if (event.getCurrentItem() != null) {
                        EnchantUtils.addEnchantment(event.getInventory().getItem(4), event.getCurrentItem().getItemMeta().getDisplayName(), main, (Player) event.getWhoClicked(), main.getConfig().getConfigurationSection("enchants"), false);
                        SubInventory subInventory = null;
                        for(SubInventory subInv : subInventories.values()) {
                            if(subInv.getInventory().equals(event.getInventory())) {
                                subInventory = subInv;
                                break;
                            }
                        }
                        SubInventory inventory = null;
                        if (subInventory.getType() == EnchantmentTarget.TOOL)
                            inventory = new SubInventory(enchantGUI.generateGUI(EnchantmentTarget.TOOL, event.getClickedInventory().getItem(4), "pickaxes"), EnchantmentTarget.TOOL);
                        else if (subInventory.getType() == EnchantmentTarget.WEAPON)
                            inventory = new SubInventory(enchantGUI.generateGUI(EnchantmentTarget.WEAPON, event.getClickedInventory().getItem(4), "swords"), EnchantmentTarget.WEAPON);
                        else if (subInventory.getType() == EnchantmentTarget.ARMOR)
                            inventory = new SubInventory(enchantGUI.generateGUI(EnchantmentTarget.ARMOR, event.getClickedInventory().getItem(4), "armor"), EnchantmentTarget.ARMOR);
                        else if (subInventory.getType() == EnchantmentTarget.BOW)
                            inventory = new SubInventory(enchantGUI.generateGUI(EnchantmentTarget.BOW, event.getClickedInventory().getItem(4), "bows"), EnchantmentTarget.BOW);
                        assert inventory != null;
                        removing.add((Player) event.getWhoClicked());
                        subInventories.replace((Player) event.getWhoClicked(), inventory);
                        event.getWhoClicked().openInventory(inventory.getInventory());
                    }
                }
            }
        } catch (NullPointerException ignored) {
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
            try {
                if (cmd.inventories.get(event.getPlayer()).equals(event.getInventory())) {
                    if (removing.contains(event.getPlayer())) {
                        removing.remove(event.getPlayer());
                    } else {
                        event.getPlayer().openInventory(event.getInventory());
                    }
                } else if (subInventories.get(event.getPlayer()).equals(event.getInventory())) {
                    if (removing.contains(event.getPlayer()))
                        removing.remove(event.getPlayer());
                    else {
                        event.getPlayer().openInventory(event.getInventory());
                    }
                }
            } catch (NullPointerException ignored) {
            }
        });
    }

    public static String getRomanNumeral(int level) {
        String numeral = c[level / 100] +
                x[(level % 100) / 10] +
                i[level % 10];
        return numeral;
    }
}
