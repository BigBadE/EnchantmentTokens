package software.bigbade.enchantmenttokens.api;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class VanillaEnchant extends CustomEnchantment {
    private Enchantment enchantment;
    private EnchantmentTarget target;

    public VanillaEnchant(Enchantment enchantment) {
        super(enchantment.getKey(), Material.BEDROCK, capitalizeString(enchantment.getKey().getKey()));
        this.enchantment = enchantment;
        setMaxLevel(enchantment.getMaxLevel());
        setStartLevel(enchantment.getStartLevel());
        target = enchantment.getItemTarget();
        setTreasure(enchantment.isTreasure());
    }

    @Override
    public boolean canEnchantItem(@Nonnull ItemStack itemStack) {
        return target.includes(itemStack.getType());
    }

    @Override
    public Enchantment getEnchantment() {
        return enchantment;
    }

    @Override
    public boolean conflictsWith(@Nonnull Enchantment other) {
        return enchantment.conflictsWith(enchantment);
    }

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i])) {
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Enchantment && enchantment.hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
        return enchantment.getKey().hashCode();
    }
}