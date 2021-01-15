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

package com.bigbade.enchantmenttokens.commands;

import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.bigbade.enchantmenttokens.EnchantmentTokens;
import com.bigbade.enchantmenttokens.localization.LocaleMessages;

import javax.annotation.Nonnull;
import java.util.Locale;

@RequiredArgsConstructor
public class RecompileEnchantsCmd implements CommandExecutor {
    private final EnchantmentTokens main;

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        Locale locale = CommandUtils.getLocale(sender, main.getPlayerHandler());
        if (!sender.hasPermission("enchanttoken.admin") && !sender.isOp()) {
            sender.sendMessage(ChatColor.stripColor(LocaleMessages.COMMAND_ERROR_PERMISSION.translate(locale)));
        } else {
            main.unregisterEnchants();
            main.registerEnchants();
        }
        return true;
    }
}
