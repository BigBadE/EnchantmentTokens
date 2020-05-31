/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.utils.EnchantPickerUtils;
import software.bigbade.enchantmenttokens.utils.ReflectionManager;

import java.lang.reflect.Method;

public class EnchantmentTablePacketHandler extends PacketAdapter {
    private static final Class<?> ENTITY_HUMAN_CLASS = ReflectionManager.getClass("net.minecraft.server.v1_" + ReflectionManager.VERSION + "_R1.EntityHuman");
    private static final Method SEED_METHOD = ReflectionManager.getMethod(ENTITY_HUMAN_CLASS, "el");
    private static final Method ENCHANT_METHOD = ReflectionManager.getMethod(ENTITY_HUMAN_CLASS, "enchantDone", EnchantPickerUtils.NMS_ITEMSTACK_CLASS, Integer.TYPE);
    private static final Method HANDLE_METHOD = ReflectionManager.getMethod(ReflectionManager.getClass("org.bukkit.craftbukkit.v1_" + ReflectionManager.VERSION + "_R1.entity.CraftPlayer"), "getHandle");

    public EnchantmentTablePacketHandler(EnchantmentTokens main) {
        super(main, ListenerPriority.NORMAL, PacketType.Play.Server.WINDOW_DATA);
    }

    @SuppressWarnings("ConstantConditions")
    public static int getSeed(Player player) {
        return (int) ReflectionManager.invoke(SEED_METHOD, ReflectionManager.invoke(HANDLE_METHOD, player));
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
