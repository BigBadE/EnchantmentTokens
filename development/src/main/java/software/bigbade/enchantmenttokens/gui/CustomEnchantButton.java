/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.gui;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

public class CustomEnchantButton implements EnchantButton {
    private final Function<EnchantmentPlayer, EnchantmentGUI> callable;
    private ItemStack item;

    @Getter
    private String translationString = null;

    public CustomEnchantButton(@Nonnull ItemStack item, @Nullable Function<EnchantmentPlayer, EnchantmentGUI> callable) {
        this.callable = callable;
        this.item = item;
    }

    public CustomEnchantButton(@Nonnull ItemStack item, @Nullable Function<EnchantmentPlayer, EnchantmentGUI> callable, @Nullable String translationString) {
        this.callable = callable;
        this.item = item;
        this.translationString = translationString;
    }

    @Nullable
    public EnchantmentGUI click(@Nonnull EnchantmentPlayer player) {
        if (callable == null) {
            return player.getCurrentGUI();
        }
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
}
