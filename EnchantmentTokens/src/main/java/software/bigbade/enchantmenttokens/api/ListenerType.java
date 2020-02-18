package software.bigbade.enchantmenttokens.api;

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
import org.bukkit.enchantments.EnchantmentTarget;

import java.util.Arrays;
import java.util.List;

public enum ListenerType {
    //On block starting hit
    BLOCK_DAMAGED(EnchantmentTarget.TOOL),
    //On block destroyed
    BLOCK_BREAK(EnchantmentTarget.TOOL),
    //Armor equipped
    EQUIP(EnchantmentTarget.ARMOR),
    //Armor unequipped
    UNEQUIP(EnchantmentTarget.ARMOR),
    //Item swapped to
    HELD(EnchantmentTarget.ALL),
    //Item swapped off
    SWAPPED(EnchantmentTarget.ALL),
    //On current enchantment applied to an item
    ENCHANT(EnchantmentTarget.ALL),
    //On user death
    DEATH(EnchantmentTarget.ALL),
    //Potion apply
    POTION_APPLY(EnchantmentTarget.ALL),
    //Potion remove
    POTION_REMOVE(EnchantmentTarget.ALL),
    //Shield block
    SHIELD_BLOCK(EnchantmentTarget.ALL),
    //Trident throw
    TRIDENT_THROW(EnchantmentTarget.TRIDENT),
    //Bow/crossbow shoot
    SHOOT(EnchantmentTarget.BOW, EnchantmentTarget.CROSSBOW),
    //Entity damage
    DAMAGE(EnchantmentTarget.ALL),
    //Player riptide with trident
    RIPTIDE(EnchantmentTarget.TRIDENT);
    //TODO

    private List<EnchantmentTarget> targets;

    ListenerType(EnchantmentTarget... targets) {
        this.targets = Arrays.asList(targets);
    }

    public boolean canTarget(EnchantmentTarget target) {
        if (target == EnchantmentTarget.ALL) return true;
        for (EnchantmentTarget found : targets)
            if (found == target)
                return true;
        return false;
    }

    public boolean canTarget(List<Material> materials) {
        return targets.stream().anyMatch((type) -> {
            for (Material material : materials) if (type.includes(material)) return true;
            return false;
        });
    }
}
