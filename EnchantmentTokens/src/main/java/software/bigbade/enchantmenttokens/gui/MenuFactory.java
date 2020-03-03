package software.bigbade.enchantmenttokens.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.utils.EnchantButton;

public interface MenuFactory {
    //Generate enchantment inventory with item in main hand. Should call genItemInventory
    EnchantmentGUI genInventory(Player player);

    //Generate enchantment inventory with specified item.
    EnchantmentGUI genItemInventory(EnchantmentPlayer enchantPlayer, ItemStack item);

    void addButton(EnchantButton button);
}
