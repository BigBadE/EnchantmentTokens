package software.bigbade.enchantmenttokens.api;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface EnchantmentBase {
    void onDisable();
    void loadConfig();
    long getDefaultPrice(int level);
    ConfigurationSection getPriceSection();

    @NotNull
    String getName();

    @NotNull
    NamespacedKey getKey();

    @NotNull
    EnchantmentTarget getItemTarget();

    @NotNull
    Enchantment getEnchantment();

    int getMaxLevel();
    int getStartLevel();
    boolean isTreasure();
    boolean conflictsWith(@NotNull Enchantment enchantment);
    boolean isCursed();
    boolean canEnchantItem(@NotNull ItemStack itemStack);
    void setTreasure(boolean treasure);
    void addConflict(Enchantment conflict);
    void addTargets(Material... targets);
    List<Material> getTargets();
    Material getIcon();
    void setCursed(boolean cursed);
    void setIcon(Material icon);
}
