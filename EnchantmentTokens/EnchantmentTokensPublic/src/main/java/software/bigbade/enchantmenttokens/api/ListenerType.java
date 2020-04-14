/*
 * Addons for the Custom Enchantment API in Minecraft
 * Copyright (C) 2020 BigBadE
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package software.bigbade.enchantmenttokens.api;

import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentTarget;
import software.bigbade.enchantmenttokens.api.wrappers.EnchantmentTargetWrapper;
import software.bigbade.enchantmenttokens.api.wrappers.ITargetWrapper;
import software.bigbade.enchantmenttokens.api.wrappers.MaterialTargetWrapper;

import java.util.List;

public enum ListenerType {
    //On block starting hit
    BLOCK_DAMAGED(new EnchantmentTargetWrapper(EnchantmentTarget.TOOL)),
    //On block destroyed
    BLOCK_BREAK(new EnchantmentTargetWrapper(EnchantmentTarget.TOOL)),
    //Armor equipped
    EQUIP(new EnchantmentTargetWrapper(EnchantmentTarget.ARMOR)),
    //Armor unequipped
    UNEQUIP(new EnchantmentTargetWrapper(EnchantmentTarget.ARMOR)),
    //Item swapped to
    HELD(new EnchantmentTargetWrapper(EnchantmentTarget.ALL)),
    //Item swapped off
    SWAPPED(new EnchantmentTargetWrapper(EnchantmentTarget.ALL)),
    //On current enchantment applied to an item
    ENCHANT(new EnchantmentTargetWrapper(EnchantmentTarget.ALL)),
    //On user death
    DEATH(new EnchantmentTargetWrapper(EnchantmentTarget.ALL)),
    //Potion apply
    POTION_APPLY(new EnchantmentTargetWrapper(EnchantmentTarget.ALL)),
    //Potion remove
    POTION_REMOVE(new EnchantmentTargetWrapper(EnchantmentTarget.ALL)),
    //Shield block
    SHIELD_BLOCK(new MaterialTargetWrapper("SHIELD")),
    //Trident throw
    TRIDENT_THROW(new EnchantmentTargetWrapper("TRIDENT")),
    //Bow shoot
    SHOOT(new EnchantmentTargetWrapper(EnchantmentTarget.BOW)),
    //Crossbow shoot
    CROSSBOW_SHOOT(new EnchantmentTargetWrapper("CROSSBOW")),
    //Entity damage
    DAMAGE(new EnchantmentTargetWrapper(EnchantmentTarget.ALL)),
    //Player riptide with trident
    RIPTIDE(new EnchantmentTargetWrapper("TRIDENT"));

    private ITargetWrapper target;

    ListenerType(ITargetWrapper target) {
        this.target = target;
    }

    public boolean canTarget(List<Material> materials) {
        return target.canTarget(materials);
    }

    public boolean canTarget(ITargetWrapper wrapper) {
        return target.canTarget(wrapper);
    }
}
