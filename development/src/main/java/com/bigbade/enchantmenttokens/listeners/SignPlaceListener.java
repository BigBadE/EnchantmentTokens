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

package com.bigbade.enchantmenttokens.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import com.bigbade.enchantmenttokens.api.EnchantmentBase;
import com.bigbade.enchantmenttokens.localization.LocaleMessages;
import com.bigbade.enchantmenttokens.utils.SignHandler;
import com.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import com.bigbade.enchantmenttokens.utils.players.PlayerHandler;

import java.util.Locale;

@RequiredArgsConstructor
public class SignPlaceListener implements Listener {
    private final EnchantmentHandler handler;
    private final PlayerHandler playerHandler;
    private final SignHandler packetHandler;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSignChange(SignChangeEvent event) {
        Locale locale = playerHandler.getPlayer(event.getPlayer()).getLanguage();
        if (("[" + LocaleMessages.ENCHANTMENT.translate(locale) + "]")
                .equalsIgnoreCase(ChatColor.stripColor(event.getLine(0))) && event.getLine(1) != null) {
            String name = event.getLine(1);
            assert name != null;
            for (EnchantmentBase base : handler.getAllEnchants()) {
                if (name.equalsIgnoreCase(base.getEnchantmentName())) {
                    packetHandler.addSign(event.getBlock().getLocation());
                    updateSign(base, event, locale);
                    return;
                }
            }
            event.getPlayer().sendMessage(LocaleMessages.ENCHANTMENT_ADD_FAIL.translate(locale));
        }
    }

    private static void updateSign(EnchantmentBase base, SignChangeEvent event, Locale locale) {
        event.getPlayer().sendMessage(LocaleMessages.ENCHANTMENT_ADD.translate(locale, base.getEnchantmentName()));
        event.setLine(0, "[" + LocaleMessages.ENCHANTMENT.translate(locale) + "]");
        event.setLine(1, base.getEnchantmentName());
    }
}
