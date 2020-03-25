package software.bigbade.enchantmenttokens.utils.enchants;

import software.bigbade.enchantmenttokens.api.EnchantmentBase;

import java.util.Collection;
import java.util.List;

public interface EnchantmentHandler {
    void registerEnchants(Collection<EnchantmentBase> enchantments);

    void unregisterEnchants();

    /**
     * Returns all enchantments used
     * @return All registered enchantments, including vanilla and skript enchantments
     */
    List<EnchantmentBase> getAllEnchants();

    /**
     * Gets custom enchantments
     * @return All registered custom enchantments, excluding vanilla or skript enchantments
     */
    List<EnchantmentBase> getCustomEnchants();

    /**
     * Gets Skript enchantments
     * @return All registered skript enchantments
     */
    List<EnchantmentBase> getSkriptEnchant();

    /**
     * Add Skript enchantment
     */
    void addSkriptEnchant(EnchantmentBase base);
}
