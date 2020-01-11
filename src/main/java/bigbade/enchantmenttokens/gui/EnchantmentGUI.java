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

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EnchantmentGUI {
    public Inventory genInvntory(ItemStack stack) {
        Inventory inventory = Bukkit.createInventory(null, 27, "Enchantments");
        inventory.setItem(4, stack);
        inventory.setItem(11, new ItemStack(Material.DIAMOND_PICKAXE));
        inventory.setItem(12, new ItemStack(Material.DIAMOND_SWORD));
        inventory.setItem(14, new ItemStack(Material.DIAMOND_CHESTPLATE));
        inventory.setItem(15, new ItemStack(Material.BOW));
        while(true) {
            int i = inventory.firstEmpty();
            if(i > -1 && i < 28)
                inventory.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
            else
                break;
        }
        return inventory;
    }
}
