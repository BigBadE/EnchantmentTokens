package software.bigbade.enchantmenttokens.utils;

import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.gui.EnchantmentGUI;

import java.util.function.Function;

public class EnchantButton {
    private Function<EnchantmentPlayer, EnchantmentGUI> callable;

    public EnchantButton(Function<EnchantmentPlayer, EnchantmentGUI> callable) {
        this.callable = callable;
    }

    public EnchantmentGUI click(EnchantmentPlayer player) {
        return callable.apply(player);
    }
}
