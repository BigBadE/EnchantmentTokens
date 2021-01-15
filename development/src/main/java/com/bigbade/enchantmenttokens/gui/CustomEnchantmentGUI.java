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

package com.bigbade.enchantmenttokens.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import com.bigbade.enchantmenttokens.api.EnchantmentBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomEnchantmentGUI implements EnchantmentGUI {
    private ItemStack item;
    private final Inventory inventory;
    private final Map<Integer, EnchantButton> buttons = new HashMap<>();
    private final List<EnchantmentBase> adding;

    public CustomEnchantmentGUI(Inventory inventory, List<EnchantmentBase> enchantments) {
        this.inventory = inventory;
        this.adding = enchantments;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public EnchantButton getButton(int slot) {
        return buttons.get(slot);
    }

    public void addButton(EnchantButton button, int slot) {
        inventory.setItem(slot, button.getItem());
        buttons.put(slot, button);
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    @Override
    public List<EnchantmentBase> getAddedEnchants() {
        return adding;
    }

    @Override
    public void addEnchantment(EnchantmentBase base) {
        adding.add(base);
    }
}
