package software.bigbade.enchantmenttokens.utils;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.gui.EnchantButton;
import software.bigbade.enchantmenttokens.gui.EnchantmentGUI;

import java.util.function.Function;

public class CustomEnchantButton implements EnchantButton {
    private Function<EnchantmentPlayer, EnchantmentGUI> callable;
    private ItemStack item;

    public CustomEnchantButton(@NotNull ItemStack item, @NotNull Function<EnchantmentPlayer, EnchantmentGUI> callable) {
        this.callable = callable;
        this.item = item;
    }

    @Nullable
    public EnchantmentGUI click(@NotNull EnchantmentPlayer player) {
        return callable.apply(player);
    }

    @NotNull
    public ItemStack getItem() {
        return item;
    }
}
