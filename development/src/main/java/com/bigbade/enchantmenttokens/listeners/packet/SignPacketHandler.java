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

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.nbt.NbtBase;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import com.bigbade.enchantmenttokens.EnchantmentTokens;
import com.bigbade.enchantmenttokens.api.EnchantmentBase;
import com.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import com.bigbade.enchantmenttokens.localization.LocaleMessages;
import com.bigbade.enchantmenttokens.utils.SignHandler;
import com.bigbade.enchantmenttokens.utils.enchants.EnchantUtils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;

public class SignPacketHandler implements SignHandler {
    private final Set<Location> signs = new HashSet<>();
    private final EnchantmentTokens main;

    public SignPacketHandler(ProtocolManager manager, EnchantmentTokens main) {
        manager.addPacketListener(new SignPacketLoadAdapter(main, this));
        manager.addPacketListener(new SignPacketUpdateAdapter(main, this));
        this.main = main;
    }

    public void removeSign(Location location) {
        signs.remove(location);
    }

    public Set<Location> getSigns() {
        return signs;
    }

    @Override
    public void addSign(Location sign) {
        signs.add(sign);
    }

    void handlePacket(NbtCompound compound, PacketEvent event) {
        EnchantmentPlayer enchantmentPlayer = main.getPlayerHandler().getPlayer(event.getPlayer());
        List<String> text = getText(compound);
        if (text.isEmpty()) {
            return;
        }
        if (!text.get(0).equals("[" + LocaleMessages.ENCHANTMENT.translate(enchantmentPlayer.getLanguage()) + "]")) {
            return;
        }
        main.getEnchantmentHandler().getAllEnchants().stream()
                .filter(base -> base.getEnchantmentName().equalsIgnoreCase(text.get(1)))
                .forEach(base -> {
                    if (event.getPacketType() == PacketType.Play.Server.MAP_CHUNK) {
                        signs.add(new Location(event.getPlayer().getWorld(), compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z")));
                    }
                    updateSign(base, compound, event);
                });
    }

    @Nonnull
    private static List<String> getText(NbtCompound compound) {
        List<String> text = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            try {
                text.add(compound.getString("Text" + i).split("text\":\"")[1].split("\"}")[0]);
            } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException ignored) {
                if (i != 3) {
                    return Collections.emptyList();
                } else {
                    break;
                }
            }
        }
        return text;
    }

    private void updateSign(EnchantmentBase base, NbtCompound compound, PacketEvent event) {
        Locale locale = main.getPlayerHandler().getPlayer(event.getPlayer()).getLanguage();
        String price;
        ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();
        if (base.canEnchantItem(itemStack)) {
            int level = EnchantUtils.getInstance().getLevel(itemStack, base) + 1;
            if (level <= base.getMaxLevel()) {
                price = LocaleMessages.translatePrice(locale, base.getDefaultPrice(level));
            } else {
                price = LocaleMessages.MAXED.translate(locale);
            }
        } else {
            price = LocaleMessages.NOT_APPLICABLE.translate(locale);
        }
        compound.put("Text3", "{\"extra\":[{\"text\":\""
                + ChatColor.stripColor(LocaleMessages.PRICE.translate(locale, price)) + "\"}],\"text\":\"\"}");
    }
}

class SignPacketLoadAdapter extends PacketAdapter {
    private final SignPacketHandler handler;

    public SignPacketLoadAdapter(EnchantmentTokens main, SignPacketHandler handler) {
        super(main, ListenerPriority.NORMAL, PacketType.Play.Server.MAP_CHUNK);
        this.handler = handler;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer container = event.getPacket();
        List<NbtBase<?>> compounds = container.getListNbtModifier().read(0);
        for (int i = 0; i < compounds.size(); i++) {
            NbtCompound compound = (NbtCompound) compounds.get(i);
            handler.handlePacket(compound, event);
            compounds.set(i, compound);
        }
        container.getListNbtModifier().write(0, compounds);
    }
}

class SignPacketUpdateAdapter extends PacketAdapter {
    private final SignPacketHandler handler;

    public SignPacketUpdateAdapter(EnchantmentTokens main, SignPacketHandler handler) {
        super(main, ListenerPriority.NORMAL, PacketType.Play.Server.TILE_ENTITY_DATA);
        this.handler = handler;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer container = event.getPacket();
        if (container.getIntegers().getValues().get(0) == 9) {
            NbtCompound compound = (NbtCompound) container.getNbtModifier().read(0);
            handler.handlePacket(compound, event);
            container.getNbtModifier().write(0, compound);
        }
    }
}
