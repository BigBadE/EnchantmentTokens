package bigbade.enchantmenttokens.utils;

import bigbade.enchantmenttokens.gui.EnchantmentGUI;
import org.bukkit.inventory.ItemStack;

import java.util.function.Function;

public class EnchantButton {
    private ItemStack item;
    private Function<ItemStack, EnchantmentGUI> callable;

    public EnchantButton(ItemStack item, Function<ItemStack, EnchantmentGUI> callable) {
        this.item = item;
        this.callable = callable;
    }

    public EnchantmentGUI click(ItemStack stack) {
        try {
            return callable.apply(stack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ItemStack getItem() { return item; }

    public boolean isSame(ItemStack item) {
        return this.item.equals(item);
    }
}
