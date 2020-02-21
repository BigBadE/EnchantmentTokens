package software.bigbade.enchantmenttokens.gui;

import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.utils.EnchantButton;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

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

public class EnchantmentGUI {
    private Player opener;
    private ItemStack item;
    private Inventory inventory;
    private Map<Integer, EnchantButton> buttons = new HashMap<>();

    public EnchantmentGUI(Inventory inventory) {
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public EnchantButton getButton(int slot) {
        return buttons.get(slot);
    }

    public void addButton(EnchantButton button, int slot) {
        buttons.put(slot, button);
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public Player getOpener() {
        return opener;
    }

    public void setOpener(Player opener) {
        this.opener = opener;
    }
}
