package software.bigbade.enchantmenttokens.utils;

import software.bigbade.enchantmenttokens.gui.EnchantmentGUI;
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
        return callable.apply(stack);
    }

    public ItemStack getItem() {
        return item;
    }
}
