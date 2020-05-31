/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.api;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.utils.ReflectionManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;

public class VanillaEnchant extends CustomEnchantment {
    private final Enchantment enchantment;
    private final EnchantmentTarget target;
<<<<<<< HEAD:EnchantmentTokens/EnchantmentTokensDevelopment/src/main/java/software/bigbade/enchantmenttokens/api/VanillaEnchant.java
    private final boolean enchantShell;
=======
>>>>>>> 3d705af96ebb617ac55d44878c2077b5e14535b9:EnchantmentTokensMain/EnchantmentTokensDevelopment/src/main/java/software/bigbade/enchantmenttokens/api/VanillaEnchant.java

    @Nullable
    private final Object rawEnchantment;

    private static final Class<?> NMS_ENCHANTMENT_CLASS = ReflectionManager.getClass("net.minecraft.server.v1_" + ReflectionManager.VERSION + "_R1.Enchantment");
    private static final Method GET_RAW_METHOD = ReflectionManager.getMethod(ReflectionManager.getClass("org.bukkit.craftbukkit.v1_" + ReflectionManager.VERSION + "_R1.enchantments.CraftEnchantment"), "getRaw", Enchantment.class);
    private static final Method GET_MIN_COST = ReflectionManager.getMethod(NMS_ENCHANTMENT_CLASS, "a", Integer.TYPE);
    private static final Method GET_MAX_COST = ReflectionManager.getMethod(NMS_ENCHANTMENT_CLASS, "b", Integer.TYPE);

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
            return (int) ReflectionManager.invoke(GET_MIN_COST, rawEnchantment, level);
        } else {
            return super.getMinCost(level);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public int getMaxCost(int level) {
        if (enchantShell) {
            return (int) ReflectionManager.invoke(GET_MAX_COST, rawEnchantment, level);
        } else {
            return super.getMaxCost(level);
        }
    }

    @Override
    public boolean isTreasure() {
        return enchantment.isTreasure();
    }

    @SuppressWarnings("deprecation")
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