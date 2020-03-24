package software.bigbade.enchantmenttokens.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;

public interface EnchantmentGUI {
    Inventory getInventory();

    EnchantButton getButton(int slot);

    void addButton(EnchantButton button, int slot);

    ItemStack getItem();

    void setItem(ItemStack item);

    EnchantmentPlayer getOpener();

    void setOpener(EnchantmentPlayer opener);
}
