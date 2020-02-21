package software.bigbade.enchantmenttokens.api;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class VanillaEnchant extends EnchantmentBase {
    private Enchantment enchantment;

    public VanillaEnchant(Material icon, Enchantment enchantment) {
        super(enchantment.getName(), icon);
        this.enchantment = enchantment;
        name = capitalizeString(enchantment.getKey().getKey());
        maxLevel = enchantment.getMaxLevel();
        minLevel = enchantment.getStartLevel();
        setTarget(enchantment.getItemTarget());
        setTreasure(enchantment.isTreasure());
        setCursed(enchantment.isCursed());
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return enchantment.conflictsWith(enchantment);
    }

    private String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }
}