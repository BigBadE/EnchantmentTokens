/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.utils;

import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.gui.EnchantButton;
import software.bigbade.enchantmenttokens.gui.EnchantmentGUI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

public class CustomEnchantButton implements EnchantButton {
    private Function<EnchantmentPlayer, EnchantmentGUI> callable;
    private ItemStack item;

    public CustomEnchantButton(@Nonnull ItemStack item, @Nullable Function<EnchantmentPlayer, EnchantmentGUI> callable) {
        this.callable = callable;
        this.item = item;
    }

    @Nullable
    public EnchantmentGUI click(@Nonnull EnchantmentPlayer player) {
        if(callable == null)
            return player.getCurrentGUI();
        return callable.apply(player);
    }

    @Nonnull
    public ItemStack getItem() {
        return item;
    }

    @Override
    public void setItem(ItemStack item) {
        this.item = item;
    }

    @Override
    public EnchantButton clone() {
        return new CustomEnchantButton(item.clone(), callable);
    }
}
