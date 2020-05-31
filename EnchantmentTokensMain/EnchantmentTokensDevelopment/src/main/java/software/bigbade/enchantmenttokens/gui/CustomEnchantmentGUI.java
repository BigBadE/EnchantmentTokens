/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomEnchantmentGUI implements EnchantmentGUI {
    private ItemStack item;
    private final Inventory inventory;
    private final Map<Integer, EnchantButton> buttons = new HashMap<>();
<<<<<<< HEAD:EnchantmentTokens/EnchantmentTokensDevelopment/src/main/java/software/bigbade/enchantmenttokens/gui/CustomEnchantmentGUI.java
    private final List<EnchantmentBase> adding;
=======
>>>>>>> 3d705af96ebb617ac55d44878c2077b5e14535b9:EnchantmentTokensMain/EnchantmentTokensDevelopment/src/main/java/software/bigbade/enchantmenttokens/gui/CustomEnchantmentGUI.java

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
