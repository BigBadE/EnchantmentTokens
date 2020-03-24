package software.bigbade.enchantmenttokens.utils;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.gui.CustomEnchantmentGUI;
import software.bigbade.enchantmenttokens.gui.EnchantButton;

import java.util.function.Function;

public class CustomEnchantButton implements EnchantButton {
    private Function<EnchantmentPlayer, CustomEnchantmentGUI> callable;
    private ItemStack item;

    public CustomEnchantButton(@NotNull ItemStack item, @NotNull Function<EnchantmentPlayer, CustomEnchantmentGUI> callable) {
        this.callable = callable;
        this.item = item;
    }

    @Nullable
    public CustomEnchantmentGUI click(@NotNull EnchantmentPlayer player) {
        return callable.apply(player);
    }

    @NotNull
    public ItemStack getItem() {
        return item;
    }
}
