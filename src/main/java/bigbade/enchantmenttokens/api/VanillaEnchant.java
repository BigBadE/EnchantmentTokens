package bigbade.enchantmenttokens.api;

/*
EnchantmentTokens
Copyright (C) 2019-2020 Big_Bad_E
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

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