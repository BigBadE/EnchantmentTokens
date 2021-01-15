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

package com.bigbade.enchantmenttokens.commands.tabcompleter;

import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import com.bigbade.enchantmenttokens.api.EnchantmentBase;
import com.bigbade.enchantmenttokens.api.VanillaEnchant;
import com.bigbade.enchantmenttokens.commands.CommandUtils;
import com.bigbade.enchantmenttokens.localization.LocaleMessages;
import com.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import com.bigbade.enchantmenttokens.utils.players.PlayerHandler;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class EnchantTabCompleter implements TabCompleter, IEnchantTabCompleter {
    private final EnchantmentHandler handler;
    private final PlayerHandler playerHandler;

    private static List<String> checkInt(String number, Locale locale) {
        try {
            if (number.trim().length() > 0) {
                Integer.parseInt(number);
            }
            return Collections.emptyList();
        } catch (NumberFormatException e) {
            return Collections.singletonList(ChatColor.stripColor(
                    LocaleMessages.COMMAND_ERROR_NOT_NUMBER.translate(locale, number)));
        }
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command,
                                      @Nonnull String label, @Nonnull String[] args) {
        Locale locale = CommandUtils.getLocale(sender, playerHandler);
        if (!sender.hasPermission("enchanttoken.admin") && !sender.isOp()) {
            return Collections.singletonList(ChatColor.stripColor(
                    LocaleMessages.COMMAND_ERROR_PERMISSION.translate(locale)));
        }
        if (args.length == 1) {
            return getKeys(args[0]);
        } else if (args.length == 2) {
            return EnchantTabCompleter.checkInt(args[1], locale);
        } else {
            return Collections.singletonList(ChatColor.stripColor(
                    LocaleMessages.COMMAND_ERROR_TOO_MANY_ARGUMENTS.translate(locale)));
        }
    }

    public List<String> getKeys(String name) {
        return handler.getAllEnchants().stream()
                .filter(base -> !(base instanceof VanillaEnchant) && base.getKey().toString().startsWith(name)
                        || base.getEnchantmentName().startsWith(name))
                .limit(10)
                .map(EnchantmentBase::getKey)
                .map(NamespacedKey::toString)
                .collect(Collectors.toList());
    }

    @Override
    public String getPermission() {
        return "enchanttoken.admin";
    }
}
