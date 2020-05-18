/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.gui;

import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.utils.ButtonFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

public class CustomButtonFactory extends ButtonFactory {
    @Override
    public EnchantButton createButton(@Nonnull ItemStack item, @Nullable Function<EnchantmentPlayer, EnchantmentGUI> callable) {
        return new CustomEnchantButton(item, callable);
    }

    @Override
    public EnchantButton createButton(@Nonnull ItemStack item, @Nullable Function<EnchantmentPlayer, EnchantmentGUI> callable, @Nullable String translationString) {
        return new CustomEnchantButton(item, callable, translationString);
    }
}
