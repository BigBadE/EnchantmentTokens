package software.bigbade.enchantmenttokens.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.gui.EnchantmentGUI;

import java.util.function.Function;

public class EnchantButton {
    private Function<EnchantmentPlayer, EnchantmentGUI> callable;

    public EnchantButton(@NotNull Function<EnchantmentPlayer, EnchantmentGUI> callable) {
        this.callable = callable;
    }

    /**
     * Called on item click
     * @param player The player who clicked
     * @return The inventory to open. Return null to close the inventory
     */
    @Nullable
    public EnchantmentGUI click(@NotNull EnchantmentPlayer player) {
        return callable.apply(player);
    }
}
