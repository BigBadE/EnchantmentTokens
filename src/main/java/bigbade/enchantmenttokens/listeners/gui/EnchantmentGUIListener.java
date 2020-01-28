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
import bigbade.enchantmenttokens.gui.EnchantmentPickerManager;
import bigbade.enchantmenttokens.gui.EnchantmentGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class EnchantmentGUIListener implements Listener {
    private EnchantmentPickerManager enchantmentPickerManager;
    private EnchantMenuCmd cmd;
    private EnchantmentTokens main;
    private int version;

    private static String[] c = {"", "C", "CC", "CCC", "CD", "D",
            "DC", "DCC", "DCCC", "CM"};
    private static String[] x = {"", "X", "XX", "XXX", "XL", "L",
            "LX", "LXX", "LXXX", "XC"};
    private static String[] i = {"", "I", "II", "III", "IV", "V",
            "VI", "VII", "VIII", "IX"};

    public EnchantmentGUIListener(EnchantmentTokens main, EnchantmentPickerManager enchantmentPickerManager, EnchantMenuCmd cmd, int version) {
        this.enchantmentPickerManager = enchantmentPickerManager;
        this.cmd = cmd;
        this.main = main;
        this.version = version;
    }

    private void handleClick(ItemStack item, Player player, int id) {
        SubInventory inventory = null;
        EnchantmentPlayer enchantPlayer = main.getPlayerHandler().loadPlayer(player, main.getCurrencyHandler());

        switch (id) {
            case 1:
                if (version >= 14 && EnchantmentTarget.CROSSBOW.includes(item.getType())) {
                    inventory = new SubInventory(enchantmentPickerManager.generateGUI(EnchantmentTarget.CROSSBOW, item, "Crossbow"), 1);
                }
                break;
            case 2:
                if (version >= 13) {
                    if (EnchantmentTarget.TRIDENT.includes(item.getType())) {
                        inventory = new SubInventory(enchantmentPickerManager.generateGUI(EnchantmentTarget.TRIDENT, item, "Trident"), 2);
                    } else if (version >= 9) {
                        if (EnchantmentTarget.FISHING_ROD.includes(item.getType())) {
                            inventory = new SubInventory(enchantmentPickerManager.generateGUI(EnchantmentTarget.FISHING_ROD, item, "Fishing Rod"), 2);
                        }
                    }
                }
                break;
            case 3:
                if (EnchantmentTarget.TOOL.includes(item.getType()))
                    inventory = new SubInventory(enchantmentPickerManager.generateGUI(EnchantmentTarget.TOOL, item, "Tools"), 3);
                break;
            case 4:
                if (EnchantmentTarget.WEAPON.includes(item.getType())) {
                    inventory = new SubInventory(enchantmentPickerManager.generateGUI(EnchantmentTarget.WEAPON, item, "Swords"), 4);
                }
                break;
            case 5:
                if (version == 13 || version <= 8) {
                    if (EnchantmentTarget.FISHING_ROD.includes(item.getType())) {
                        inventory = new SubInventory(enchantmentPickerManager.generateGUI(EnchantmentTarget.FISHING_ROD, item, "Fishing Rod"), 4);
                    }
                }
            case 6:
                if (EnchantmentTarget.ARMOR.includes(item.getType()))
                    inventory = new SubInventory(enchantmentPickerManager.generateGUI(EnchantmentTarget.ARMOR, item, "Armor"), 6);
                break;
            case 7:
                if (EnchantmentTarget.BOW.includes(item.getType()))
                    inventory = new SubInventory(enchantmentPickerManager.generateGUI(EnchantmentTarget.BOW, item, "Bow"), 7);
                break;
            case 8:
                if (version >= 14) {
                    if (EnchantmentTarget.TRIDENT.includes(item.getType())) {
                        inventory = new SubInventory(enchantmentPickerManager.generateGUI(EnchantmentTarget.TRIDENT, item, "crossbow"), 8);
                    }
                } else if (version >= 9) {
                    if (item.getType() == Material.SHIELD) {
                        inventory = new SubInventory(enchantmentPickerManager.generateGUI(null, item, "Shield"), 8);
                    }
                }
                break;
            case 9:
                if (version >= 14) {
                    if (item.getType() == Material.SHIELD) {
                        inventory = new SubInventory(enchantmentPickerManager.generateGUI(null, item, "Shield"), 9);
                    }
                }
                break;
            case 13:
                ItemMeta meta = item.getItemMeta();
                assert meta != null;
                if(meta.getLore() != null) {
                    String line = meta.getLore().get(meta.getLore().size() - 1);
                    long price = Long.parseLong(line.substring(9).replace("G", ""));
                    List<String> lore = meta.getLore();
                    lore.remove(meta.getLore().size() - 1);
                    meta.setLore(lore);

                    item.setItemMeta(meta);
                    enchantPlayer.addGems(-price);
                    player.getInventory().setItemInMainHand(item);
                }
                enchantPlayer.getCurrentGUI().close(player);
                break;
            case 15:
                enchantPlayer.getCurrentGUI().close(player);
        }
        if (inventory != null) {
            enchantPlayer.getCurrentGUI().close(player);
            enchantPlayer.setCurrentGUI(inventory);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        try {
            Player player = (Player) event.getWhoClicked();
            EnchantmentPlayer enchantPlayer = main.getPlayerHandler().loadPlayer(player, main.getCurrencyHandler());
            if(enchantPlayer.getCurrentGUI() == null) return;
            if (event.getInventory().equals(enchantPlayer.getCurrentGUI().getInventory())) {
                event.setCancelled(true);
                handleClick(event.getInventory().getItem(4), (Player) event.getWhoClicked(), event.getSlot() - 8);
            } else if (event.getInventory().equals(enchantPlayer.getCurrentGUI().getInventory())) {
                event.setCancelled(true);
                ItemStack clicked = event.getCurrentItem();
                if (clicked != null) {
                    if (clicked.getType() == Material.BARRIER) {
                        EnchantmentGUI current = enchantPlayer.getCurrentGUI();
                        EnchantmentGUI gui = new EnchantmentGUI(cmd.genInventory(player));
                        gui.getInventory().setItem(4, current.getInventory().getItem(4));
                        current.close(player);
                        enchantPlayer.setCurrentGUI(gui);
                    } else {
                        if (event.getCurrentItem() != null) {
                            SubInventory inventory = (SubInventory) enchantPlayer.getCurrentGUI();
                            EnchantUtils.addEnchantment(event.getInventory().getItem(4), event.getCurrentItem().getItemMeta().getDisplayName(), main, (Player) event.getWhoClicked(), false);
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
        EnchantmentPlayer enchantPlayer = main.getPlayerHandler().loadPlayer((Player) event.getPlayer(), main.getCurrencyHandler());
        Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
            if (enchantPlayer.getCurrentGUI() != null && enchantPlayer.getCurrentGUI().getInventory().equals(event.getInventory()))
                if (enchantPlayer.getCurrentGUI().isClosing())
                    event.getPlayer().openInventory(event.getInventory());
        });
    }

    public static String getRomanNumeral(int level) {
        return c[level / 100] +
                x[(level % 100) / 10] +
                i[level % 10];
    }
}
