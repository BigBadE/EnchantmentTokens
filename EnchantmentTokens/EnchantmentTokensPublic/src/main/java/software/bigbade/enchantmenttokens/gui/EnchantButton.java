package software.bigbade.enchantmenttokens.gui;

import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface EnchantButton {
    /**
     * Called on item click
     * @param player The player who clicked
     * @return The inventory to open. Return null to close the inventory
     */
    @Nullable
    EnchantmentGUI click(@Nonnull EnchantmentPlayer player);

    @Nonnull
    ItemStack getItem();
}
