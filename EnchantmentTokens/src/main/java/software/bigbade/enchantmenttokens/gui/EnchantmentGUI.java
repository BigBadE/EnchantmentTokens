package software.bigbade.enchantmenttokens.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import software.bigbade.enchantmenttokens.utils.EnchantButton;

public interface EnchantmentGUI {
    @Nullable
    Inventory getInventory();

    @Nullable
    EnchantButton getButton(int slot);

    void addButton(EnchantButton button, int slot);

    ItemStack getItem();

    void setItem(ItemStack item);
}
