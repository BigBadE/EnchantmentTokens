/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.utils;

import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public final class EnchantPickerUtils {
    private EnchantPickerUtils() { }

    public static final Class<?> NMS_ITEMSTACK_CLASS = ReflectionManager.getClass("net.minecraft.server.v1_"  + ReflectionManager.VERSION +  "_R1.ItemStack");

    private static final Method NMS_STACK_METHOD = ReflectionManager.getMethod(ReflectionManager.getClass("org.bukkit.craftbukkit.v1_" + ReflectionManager.VERSION + "_R1.inventory.CraftItemStack"), "asNMSCopy", ItemStack.class);
    private static final Method GET_ENCHANTABILITY = ReflectionManager.getMethod(ReflectionManager.getClass("net.minecraft.server.v1_" + ReflectionManager.VERSION + "_R1.Item"), "c");
    private static final Method GET_ITEM = ReflectionManager.getMethod(NMS_ITEMSTACK_CLASS, "getItem");

    public static Object getNMSItemStack(ItemStack item) {
        return ReflectionManager.invoke(NMS_STACK_METHOD, null, item);
    }

    @SuppressWarnings("ConstantConditions")
    private static int getEnchantability(ItemStack item) {
        return ReflectionManager.invoke(GET_ENCHANTABILITY, ReflectionManager.invoke(GET_ITEM, getNMSItemStack(item)));
    }

    public static int getRequiredExperience(Random random, int slot, int bookshelves, int maxLevel, ItemStack itemstack) {
        int enchantability = getEnchantability(itemstack);

        if (enchantability <= 0) {
            return 0;
        } else {
            if (bookshelves > 15) {
                bookshelves = 15;
            }

            int experience = random.nextInt(8) + 1 + (bookshelves >> 1) + random.nextInt(bookshelves + 1) * (maxLevel/30);

            switch(slot) {
                case 0:
                    return Math.max(experience / 3, 1);
                case 1:
                    return experience * 2 / 3 + 1;
                case 2:
                    return Math.max(experience, bookshelves * 2);
                default:
                    throw new IllegalStateException("Slot is over 2!");
            }
        }
    }

    public static Set<EnchantmentBase> rollEnchantments(List<EnchantmentBase> enchantments, Random random, ItemStack itemstack, int cost, boolean flag) {
        Set<EnchantmentBase> list = new HashSet<>();
        int enchantability = getEnchantability(itemstack);

        cost += 1 + random.nextInt(enchantability / 4 + 1) + random.nextInt(enchantability / 4 + 1);
        float randomFloat = (random.nextFloat() + random.nextFloat() - 1.0F) * 0.15F;

        cost = clamp(Math.round((float) cost + (float) cost * randomFloat), 1, Integer.MAX_VALUE);
        Map<EnchantmentBase, Integer> list1 = getEnchantments(enchantments, cost, itemstack, flag);

        if (!list1.isEmpty()) {
            list.add(getWeightedElement(random, list1.keySet()));
            while (random.nextInt(50) <= cost) {
                for(EnchantmentBase base : list) {
                    removeConflicts(list1.keySet(), base);
                }

                if (list1.isEmpty()) {
                    break;
                }

                list.add(getWeightedElement(random, list1.keySet()));
                cost /= 2;
            }
        }

        return list;
    }

    public static void removeConflicts(Set<EnchantmentBase> list, EnchantmentBase weightedrandomenchant) {
        list.removeIf(enchantmentBase -> !weightedrandomenchant.conflictsWith(enchantmentBase.getEnchantment()));
    }

    public static EnchantmentBase getWeightedElement(Random random, Set<EnchantmentBase> enchants) {
        int maxRarity = 0;
        for(EnchantmentBase base : enchants) {
            maxRarity += base.getRarity();
        }
        int index = random.nextInt(maxRarity);
        int current = 0;
        for(EnchantmentBase base : enchants) {
            if(index <= current) {
                return base;
            }
            current += 1;
        }
        return null;
    }

    public static Map<EnchantmentBase, Integer> getEnchantments(List<EnchantmentBase> enchantments, int cost, ItemStack itemstack, boolean flag) {
        Map<EnchantmentBase, Integer> list = Maps.newHashMap();
        Material item = itemstack.getType();
        boolean flag1 = item == Material.BOOK;

        for (EnchantmentBase enchantment : enchantments) {
            if ((!enchantment.isTreasure() || flag) && (enchantment.canEnchantItem(itemstack) || flag1)) {
                for (int level = enchantment.getMaxTableLevel(); level > enchantment.getStartLevel() - 1; --level) {
                    if (cost >= enchantment.getMinCost(level) && cost <= enchantment.getMaxCost(level)) {
                        list.put(enchantment, level);
                        break;
                    }
                }
            }
        }

        return list;
    }

    public static int clamp(int value, int min, int max) {
        return value < min ? min : (Math.min(value, max));
    }
}
