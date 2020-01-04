package bigbade.enchantmenttokens.gui;

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
