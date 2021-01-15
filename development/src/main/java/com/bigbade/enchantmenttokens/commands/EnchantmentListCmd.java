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

import com.bigbade.enchantmenttokens.api.EnchantmentBase;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.bigbade.enchantmenttokens.localization.LocaleMessages;
import com.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import com.bigbade.enchantmenttokens.utils.players.PlayerHandler;

import javax.annotation.Nonnull;
import java.util.Locale;

@RequiredArgsConstructor
public class EnchantmentListCmd implements CommandExecutor {
    private final EnchantmentHandler handler;
    private final PlayerHandler playerHandler;

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        Locale locale = CommandUtils.getLocale(sender, playerHandler);
        if (!sender.hasPermission("enchanttoken.list") && !sender.isOp()) {
            sender.sendMessage(LocaleMessages.COMMAND_ERROR_PERMISSION.translate(locale));
        } else {
            StringBuilder builder = new StringBuilder();
            for(EnchantmentBase enchant : handler.getAllEnchants()) {
                builder.append(enchant.getEnchantmentName()).append(", ");
            }
            if (builder.length() > 0) {
                builder.setLength(builder.length() - 2);
            }
            sender.sendMessage(LocaleMessages.COMMAND_LIST.translate(locale, handler.getAllEnchants().size(), builder));
        }
        return true;
    }
}
