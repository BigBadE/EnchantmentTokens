package software.bigbade.enchantmenttokens.utils;

import software.bigbade.enchantmenttokens.gui.EnchantmentGUI;
import org.bukkit.inventory.ItemStack;

import java.util.function.Function;

public class EnchantButton {
    private Function<ItemStack, EnchantmentGUI> callable;

    public EnchantButton(Function<ItemStack, EnchantmentGUI> callable) {
        this.callable = callable;
    }

    public EnchantmentGUI click(ItemStack stack) {
        return callable.apply(stack);
    }
}
