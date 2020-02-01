package bigbade.enchantmenttokens.gui;

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

import bigbade.enchantmenttokens.api.EnchantmentBase;
import bigbade.enchantmenttokens.api.VanillaEnchant;
import bigbade.enchantmenttokens.utils.EnchantmentHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class EnchantmentPickerManager {
    //Main class
    private EnchantmentHandler handler;
    //Configuration section for vanilla enchantments
    private ConfigurationSection section;

    //Basic grey pane used for populating the GUI.
    private ItemStack greyPlane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
    //Barrier used for exiting the GUI
    private ItemStack exit = new ItemStack(Material.BARRIER);

    public EnchantmentPickerManager(EnchantmentHandler handler, ConfigurationSection section) {
        this.handler = handler;
        this.section = section;
        //Setup exit item
        ItemMeta meta = exit.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GREEN + "Back to enchantments");
        exit.setItemMeta(meta);
    }

    /**
     * Generate the GUI with every enchantment in it
     * @param target target item to generate the GUI for. If this is null, it checks by them item type
     * @param itemStack ItemStack that items are being added to
     * @param name Name of the material
     * @return Generated enchantment inventory
     */
    public Inventory generateGUI(EnchantmentTarget target, ItemStack itemStack, String name) {
        Inventory inventory = Bukkit.createInventory(null, 54, "Enchantments for " + name);

        for (int i = 0; i < 9; i++)
            inventory.setItem(i, greyPlane);
        for (int i = 8; i < 45; i++) {
            inventory.setItem(i, greyPlane);
            if (i % 9 == 0) {
                i += 7;
            }
        }
        for (int i = 45; i < 54; i++) {
            inventory.setItem(i, greyPlane);
        }

        inventory.setItem(4, itemStack);

        for (VanillaEnchant enchantment : handler.getVanillaEnchants()) {
            if (enchantment.getItemTarget() == target || enchantment.getItemTarget() == EnchantmentTarget.ALL) {
                ItemStack item = new ItemStack(enchantment.getIcon());
                for (Map.Entry<Enchantment, Integer> enchantment1 : itemStack.getEnchantments().entrySet()) {
                    if (enchantment1.getKey().getKey().equals(enchantment.getKey())) {
                        item.setAmount(enchantment1.getValue() + 1);
                        if (item.getAmount() > enchantment.getMaxLevel())
                            item.setAmount(0);
                        break;
                    }
                }
                ItemMeta meta = item.getItemMeta();
                assert meta != null;
                meta.setDisplayName(ChatColor.GREEN + enchantment.getName());
                meta.setLore(Arrays.asList(ChatColor.GRAY + "Price: " + section.getConfigurationSection(enchantment.getName()).getConfigurationSection("prices").getLong("" + item.getAmount()), ChatColor.GRAY + "Level: " + item.getAmount()));
                item.setItemMeta(meta);
                inventory.addItem(item);
            }
        }
        //TODO translate
        for (EnchantmentBase enchantment : handler.getEnchantments()) {
            if (enchantment.getItemTarget() == target || enchantment.getItemTarget() == EnchantmentTarget.ALL || enchantment.getTargets().contains(itemStack.getType())) {
                ItemStack item = new ItemStack(enchantment.getIcon());
                for (Map.Entry<Enchantment, Integer> enchantment1 : itemStack.getEnchantments().entrySet()) {
                    if (enchantment1.getKey().getKey().equals(enchantment.getKey())) {
                        item.setAmount(enchantment1.getValue());
                        if (item.getAmount() > enchantment.getMaxLevel())
                            item.setAmount(0);
                        break;
                    }
                }
                ItemMeta meta = item.getItemMeta();
                assert meta != null;
                meta.setDisplayName(enchantment.getName());
                if (item.getAmount() > 0) {
                    meta.setLore(Arrays.asList(ChatColor.GRAY + "Price: " + enchantment.getDefaultPrice(item.getAmount()), ChatColor.GRAY + "Level: " + item.getAmount()));
                } else
                    meta.setLore(Collections.singletonList(ChatColor.GRAY + "MAXED"));
                item.setItemMeta(meta);
                inventory.addItem(item);

            }
        }
        inventory.setItem(49, exit);
        return inventory;
    }
}
