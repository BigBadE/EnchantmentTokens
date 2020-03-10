package software.bigbade.enchantmenttokens.utils;

import com.mojang.brigadier.CommandDispatcher;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.gui.EnchantmentGUI;

import java.util.function.Function;

public class EnchantButton {
    private Function<EnchantmentPlayer, EnchantmentGUI> callable;
    private ItemStack item;

    public EnchantButton(@NotNull ItemStack item, @NotNull Function<EnchantmentPlayer, EnchantmentGUI> callable) {
        this.callable = callable;
        this.item = item;
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

    @NotNull
    public ItemStack getItem() {
        return item;
    }
}
