package software.bigbade.enchantmenttokens.api;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.api.wrappers.ITargetWrapper;

import javax.annotation.Nonnull;

public interface EnchantmentBase {
    @Nonnull
    NamespacedKey getKey();

    Material getIcon();

    void setIcon(Material icon);

    int getStartLevel();

    int getMaxLevel();

    void onDisable();

    long getDefaultPrice(int level);

    void loadConfig();

    boolean canEnchantItem(ItemStack item);

    Enchantment getEnchantment();

    ConfigurationSection getPrice();

    void setTarget(ITargetWrapper target);

    ITargetWrapper getTarget();

    @Nonnull
    String getEnchantmentName();
}
