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

import bigbade.enchantmenttokens.EnchantmentTokens;
import bigbade.enchantmenttokens.api.EnchantUtils;
import bigbade.enchantmenttokens.api.EnchantmentBase;
import bigbade.enchantmenttokens.api.SubInventory;
import bigbade.enchantmenttokens.api.VanillaEnchant;
import bigbade.enchantmenttokens.localization.TranslatedMessage;
import bigbade.enchantmenttokens.utils.EnchantButton;
import bigbade.enchantmenttokens.utils.EnchantmentHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class EnchantmentPickerManager {
    //Main class
    private EnchantmentHandler handler;

    private EnchantmentMenuFactory factory;

    private EnchantUtils utils;

    //Basic grey pane used for populating the GUI.
    private ItemStack greyPlane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
    //Barrier used for exiting the GUI
    private ItemStack exit = new ItemStack(Material.BARRIER);

    public EnchantmentPickerManager(EnchantmentTokens main, ConfigurationSection section) {
        handler = main.getEnchantmentHandler();
        factory = main.getFactory();
        utils = main.getUtils();
        //Setup exit item
        ItemMeta meta = exit.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GREEN + "Back to enchantments");
        exit.setItemMeta(meta);
    }

    /**
     * Generate the GUI with every enchantment in it
     *
     * @param target    target item to generate the GUI for. If this is null, it checks by them item type
     * @param itemStack ItemStack that items are being added to
     * @param name      Name of the material
     * @return Generated enchantment inventory
     */
    public SubInventory generateGUI(EnchantmentTarget target, ItemStack itemStack, Player player, String name) {
        Inventory inventory = Bukkit.createInventory(null, 54, name);

        SubInventory subInventory = new SubInventory(inventory);

        subInventory.setItem(itemStack);

        for (int i = 0; i < 54; i++) {
            if (i < 10 || i > 45 || i % 9 == 0 || i % 9 == 8)
                inventory.setItem(i, greyPlane);
        }

        inventory.setItem(4, itemStack);

        for (VanillaEnchant enchantment : handler.getVanillaEnchants()) {
            if (enchantment.getItemTarget() != target && enchantment.getItemTarget() != EnchantmentTarget.ALL)
                continue;

            subInventory.addButton(updateItem(name, enchantment, itemStack, player));
        }

        for (EnchantmentBase enchantment : handler.getEnchantments()) {
            if (enchantment.getItemTarget() != target && enchantment.getItemTarget() != EnchantmentTarget.ALL && !enchantment.getTargets().contains(itemStack.getType()))
                continue;

            subInventory.addButton(updateItem(name, enchantment, itemStack, player));
        }
        inventory.setItem(49, exit);
        subInventory.addButton(new EnchantButton(EnchantmentMenuFactory.makeItem(Material.BARRIER, TranslatedMessage.translate("enchant.back")), (item) -> factory.genInventory(player)));
        return subInventory;
    }

    private EnchantButton updateItem(String name, EnchantmentBase base, ItemStack stack, Player player) {
        ItemStack item = EnchantmentMenuFactory.makeItem(base.getIcon(), base.getName());
        item.setAmount(getLevel(stack, base) + 1);
        return new EnchantButton(item, (itemStack) -> {
            utils.addEnchantmentBase(itemStack, base, player, false);
            return generateGUI(base.getItemTarget(), itemStack, player, name);
        });
    }

    private int getLevel(ItemStack stack, EnchantmentBase base) {
        for(Map.Entry<Enchantment, Integer> enchantment : stack.getEnchantments().entrySet()) {
            if(enchantment.getKey().getKey().equals(base.getKey()))
                return enchantment.getValue();
        }
        return 0;
    }
}
