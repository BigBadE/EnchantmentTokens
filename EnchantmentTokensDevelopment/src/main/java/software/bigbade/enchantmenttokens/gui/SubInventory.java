/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.gui;

import org.bukkit.inventory.Inventory;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;

import java.util.List;

public class SubInventory extends CustomEnchantmentGUI {
    public SubInventory(Inventory inventory, List<EnchantmentBase> enchantments) {
        super(inventory, enchantments);
    }
}
