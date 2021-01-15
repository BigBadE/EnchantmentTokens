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

package com.bigbade.enchantmenttokens.listeners.packet;

import com.bigbade.enchantmenttokens.EnchantmentTokens;
import com.bigbade.enchantmenttokens.utils.EnchantPickerUtils;
import com.bigbade.enchantmenttokens.utils.ReflectionManager;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

public class EnchantmentTablePacketHandler extends PacketAdapter {
    private static final Class<?> ENTITY_HUMAN_CLASS = ReflectionManager.getClass("net.minecraft.server."
            + ReflectionManager.NMS_VERSION + ".EntityHuman");
    private static final Method SEED_METHOD = getSeedMethod();
    private static final Method ENCHANT_METHOD = ReflectionManager.getMethod(ENTITY_HUMAN_CLASS, "enchantDone",
            EnchantPickerUtils.NMS_ITEMSTACK_CLASS, Integer.TYPE);
    private static final Method HANDLE_METHOD = ReflectionManager.getMethod(ReflectionManager.getClass(
            "org.bukkit.craftbukkit." + ReflectionManager.NMS_VERSION + ".entity.CraftPlayer"), "getHandle");

    public EnchantmentTablePacketHandler(EnchantmentTokens main) {
        super(main, ListenerPriority.NORMAL, PacketType.Play.Server.WINDOW_DATA);
    }

    private static Method getSeedMethod() {
        try {
            //Don't care about other old versions.
            switch (ReflectionManager.VERSION) {
                case 8:
                    return ReflectionManager.getMethod(ENTITY_HUMAN_CLASS, "cj");
                case 12:
                    return ReflectionManager.getMethod(ENTITY_HUMAN_CLASS, "dg");
                case 13:
                    return ReflectionManager.getMethod(ENTITY_HUMAN_CLASS, "du");
                case 14:
                    //This one varies randomly from 1.14.3 to 1.14.4
                    try {
                        return ReflectionManager.getMethod(ENTITY_HUMAN_CLASS, "dM");
                    } catch (IllegalStateException e) {
                        return ReflectionManager.getMethod(ENTITY_HUMAN_CLASS, "dN");
                    }
                case 15:
                    return ReflectionManager.getMethod(ENTITY_HUMAN_CLASS, "el");
                case 16:
                default:
                    return ReflectionManager.getMethod(ENTITY_HUMAN_CLASS, "eF");
            }
        } catch (IllegalStateException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.INFO, "Unrecognized version prevents " +
                    "getting the player's enchantment seed, a random seed will be used instead");
        }
        return null;
    }

    @SuppressWarnings("ConstantConditions")
    public static int getSeed(Player player) {
        if (SEED_METHOD == null) {
            return ThreadLocalRandom.current().nextInt();
        }
        return ReflectionManager.invoke(SEED_METHOD, ReflectionManager.invoke(HANDLE_METHOD, player));
    }

    public static void onEnchant(Player player, ItemStack item, int level) {
        ReflectionManager.invoke(ENCHANT_METHOD, ReflectionManager.invoke(HANDLE_METHOD, player), EnchantPickerUtils.getNMSItemStack(item), level);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer container = event.getPacket();
        Inventory topInventory = event.getPlayer().getOpenInventory().getTopInventory();
        if (topInventory instanceof EnchantingInventory) {
            EnchantingInventory enchantingInventory = (EnchantingInventory) topInventory;
            if (enchantingInventory.getItem() == null) {
                return;
            }
            int property = container.getIntegers().read(1);
            int value = container.getIntegers().read(2);
            if (property >= 4 && 6 >= property) {
                value = -1;
            }
            container.getIntegers().write(2, value);
        }
    }
}
