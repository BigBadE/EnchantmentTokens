package software.bigbade.enchantmenttokens.gui;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;

public interface EnchantButton {
    /**
     * Called on item click
     * @param player The player who clicked
     * @return The inventory to open. Return null to close the inventory
     */
    @Nullable
    EnchantmentGUI click(@NotNull EnchantmentPlayer player);

    @NotNull
    ItemStack getItem();
}
