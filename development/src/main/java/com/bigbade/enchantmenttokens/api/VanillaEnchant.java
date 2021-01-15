/*
 * Custom enchantments for Minecraft
 * Copyright (C) 2021 Big_Bad_E
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

package com.bigbade.enchantmenttokens.api;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import com.bigbade.enchantmenttokens.utils.ReflectionManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;

public class VanillaEnchant extends CustomEnchantment {
    private final Enchantment enchantment;
    private final EnchantmentTarget target;
    private final boolean enchantShell;

    @Nullable
    private final Object rawEnchantment;

    private static final Class<?> NMS_ENCHANTMENT_CLASS = ReflectionManager.getClass("net.minecraft.server."
            + ReflectionManager.NMS_VERSION + ".Enchantment");
    private static final Method GET_RAW_METHOD = ReflectionManager.getMethod(ReflectionManager.getClass(
            "org.bukkit.craftbukkit." + ReflectionManager.NMS_VERSION + ".enchantments.CraftEnchantment"),
            "getRaw", Enchantment.class);
    private static final Method GET_MIN_COST = ReflectionManager.getMethod(NMS_ENCHANTMENT_CLASS, "a",
            Integer.TYPE);
    private static final Method GET_MAX_COST = ReflectionManager.getMethod(NMS_ENCHANTMENT_CLASS, "b",
            Integer.TYPE);

    public VanillaEnchant(Enchantment enchantment, boolean enchantShell) {
        super(enchantment.getKey(), Material.BEDROCK, capitalizeString(enchantment.getKey().getKey()));
        this.enchantment = enchantment;
        this.enchantShell = enchantShell;
        setMaxLevel(enchantment.getMaxLevel());
        setStartLevel(enchantment.getStartLevel());
        target = enchantment.getItemTarget();
        setTreasure(enchantment.isTreasure());
        if(enchantShell) {
            rawEnchantment = ReflectionManager.invoke(GET_RAW_METHOD, null, enchantment);
        } else {
            rawEnchantment = null;
        }
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

    @Override
    public int getMaxTableLevel() {
        if (enchantShell) {
            return enchantment.getMaxLevel();
        } else {
            return super.getMaxTableLevel();
        }
    }

    @Override
    public int getStartLevel() {
        if (enchantShell) {
            return enchantment.getStartLevel();
        } else {
            return super.getStartLevel();
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public int getMinCost(int level) {
        if (enchantShell) {
            return ReflectionManager.invoke(GET_MIN_COST, rawEnchantment, level);
        } else {
            return super.getMinCost(level);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public int getMaxCost(int level) {
        if (enchantShell) {
            return ReflectionManager.invoke(GET_MAX_COST, rawEnchantment, level);
        } else {
            return super.getMaxCost(level);
        }
    }

    @Override
    public boolean isTreasure() {
        return enchantment.isTreasure();
    }

    /**
     * @return If the enchant is cursed
     * @deprecated
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public boolean isCursed() {
        return enchantment.isCursed();
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
