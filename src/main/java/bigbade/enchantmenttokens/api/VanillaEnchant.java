package bigbade.enchantmenttokens.api;

import bigbade.enchantmenttokens.EnchantmentTokens;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;

public class VanillaEnchant extends EnchantmentBase {
    public Enchantment enchantment;

    public VanillaEnchant(EnchantmentTokens main, ConfigurationSection config, Material icon, Enchantment enchantment) {
        super(main, enchantment.getName(), config, icon);
        this.enchantment = enchantment;
    }
}