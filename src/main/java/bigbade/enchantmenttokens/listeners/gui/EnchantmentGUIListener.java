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
import bigbade.enchantmenttokens.gui.EnchantPickerGUI;
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
    private EnchantPickerGUI enchantPickerGUI;
    private Map<Player, SubInventory> subInventories = new HashMap<>();
    private Set<Player> removing = new HashSet<>();
    private EnchantMenuCmd cmd;
    private EnchantmentTokens main;
    private int version;

    private static String[] c = {"", "C", "CC", "CCC", "CD", "D",
            "DC", "DCC", "DCCC", "CM"};
    private static String[] x = {"", "X", "XX", "XXX", "XL", "L",
            "LX", "LXX", "LXXX", "XC"};
    private static String[] i = {"", "I", "II", "III", "IV", "V",
            "VI", "VII", "VIII", "IX"};

    public EnchantmentGUIListener(EnchantmentTokens main, EnchantPickerGUI enchantPickerGUI, EnchantMenuCmd cmd, int version) {
        this.enchantPickerGUI = enchantPickerGUI;
        this.cmd = cmd;
        this.main = main;
        this.version = version;
    }

    private void handleClick(ItemStack item, Player player, int id) {
        SubInventory inventory = null;
        switch (id) {
            case 1:
                if (version >= 14 && EnchantmentTarget.CROSSBOW.includes(item.getType())) {
                    inventory = new SubInventory(enchantPickerGUI.generateGUI(EnchantmentTarget.CROSSBOW, item, "Crossbow"), 1);
                }
                break;
            case 2:
                if (version >= 13) {
                    if (EnchantmentTarget.TRIDENT.includes(item.getType())) {
                        inventory = new SubInventory(enchantPickerGUI.generateGUI(EnchantmentTarget.TRIDENT, item, "Trident"), 2);
                    } else if (version >= 9) {
                        if (EnchantmentTarget.FISHING_ROD.includes(item.getType())) {
                            inventory = new SubInventory(enchantPickerGUI.generateGUI(EnchantmentTarget.FISHING_ROD, item, "Fishing Rod"), 2);
                        }
                    }
                }
                break;
            case 3:
                if (EnchantmentTarget.TOOL.includes(item.getType()))
                    inventory = new SubInventory(enchantPickerGUI.generateGUI(EnchantmentTarget.TOOL, item, "Tools"), 3);
                break;
            case 4:
                if (EnchantmentTarget.WEAPON.includes(item.getType())) {
                    inventory = new SubInventory(enchantPickerGUI.generateGUI(EnchantmentTarget.WEAPON, item, "Swords"), 4);
                }
                break;
            case 5:
                if (version == 13 || version <= 8) {
                    if (EnchantmentTarget.FISHING_ROD.includes(item.getType())) {
                        inventory = new SubInventory(enchantPickerGUI.generateGUI(EnchantmentTarget.FISHING_ROD, item, "Fishing Rod"), 4);
                    }
                }
            case 6:
                if (EnchantmentTarget.ARMOR.includes(item.getType()))
                    inventory = new SubInventory(enchantPickerGUI.generateGUI(EnchantmentTarget.ARMOR, item, "Armor"), 6);
                break;
            case 7:
                if (EnchantmentTarget.BOW.includes(item.getType()))
                    inventory = new SubInventory(enchantPickerGUI.generateGUI(EnchantmentTarget.BOW, item, "Bow"), 7);
                break;
            case 8:
                if (version >= 14) {
                    if (EnchantmentTarget.TRIDENT.includes(item.getType())) {
                        inventory = new SubInventory(enchantPickerGUI.generateGUI(EnchantmentTarget.TRIDENT, item, "crossbow"), 8);
                    }
                } else if (version >= 9) {
                    if (item.getType() == Material.SHIELD) {
                        inventory = new SubInventory(enchantPickerGUI.generateGUI(null, item, "Shield"), 8);
                    }
                }
                break;
            case 9:
                if (version >= 14) {
                    if (item.getType() == Material.SHIELD) {
                        inventory = new SubInventory(enchantPickerGUI.generateGUI(null, item, "Shield"), 9);
                    }
                }
                break;
            case 13:
                ItemMeta meta = item.getItemMeta();
                String line = meta.getLore().get(meta.getLore().size() - 1);
                long price = Long.parseLong(line.substring(9).replace("G", ""));
                List<String> lore = meta.getLore();
                lore.remove(meta.getLore().size() - 1);
                meta.setLore(lore);
                EnchantmentPlayer enchantPlayer = main.fileLoader.loadPlayer(player);

                item.setItemMeta(meta);
                enchantPlayer.addGems(-price);
                player.getPlayer().getInventory().setItemInMainHand(item);
                removing.add(player);
                player.closeInventory();
                break;
            case 15:
                removing.add(player);
                player.closeInventory();
        }
        if (inventory != null) {
            removing.add(player);
            player.closeInventory();
            player.openInventory(inventory.getInventory());
            subInventories.put(player, inventory);
        }
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        try {
            if (event.getInventory().equals(cmd.inventories.get(event.getWhoClicked()))) {
                event.setCancelled(true);
                handleClick(event.getInventory().getItem(4), (Player) event.getWhoClicked(), event.getSlot()-8);
            } else if (event.getInventory().equals(subInventories.get(event.getWhoClicked()).getInventory())) {
                event.setCancelled(true);
                ItemStack clicked = event.getCurrentItem();
                if(clicked != null) {
                    if (clicked.getType() == Material.BARRIER) {
                        Inventory inventory = cmd.inventories.get(event.getWhoClicked());
                        inventory.setItem(4, event.getClickedInventory().getItem(4));
                        cmd.inventories.replace((Player) event.getWhoClicked(), inventory);
                        removing.add((Player) event.getWhoClicked());
                        subInventories.remove(event.getWhoClicked());
                        event.getWhoClicked().closeInventory();
                        event.getWhoClicked().openInventory(inventory);
                    } else {
                        if (event.getCurrentItem() != null) {
                            SubInventory inventory = subInventories.get(event.getWhoClicked());
                            EnchantUtils.addEnchantment(event.getInventory().getItem(4), event.getCurrentItem().getItemMeta().getDisplayName(), main, (Player) event.getWhoClicked(), main.getConfig().getConfigurationSection("enchants"), false);
                            handleClick(event.getInventory().getItem(4), (Player) event.getWhoClicked(), inventory.getMaterial());
                        }
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
                } else if (subInventories.get(event.getPlayer()).getInventory().equals(event.getInventory())) {
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
        return c[level / 100] +
                x[(level % 100) / 10] +
                i[level % 10];
    }
}
