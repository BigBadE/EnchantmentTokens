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
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import com.bigbade.enchantmenttokens.localization.LocaleMessages;
import com.bigbade.enchantmenttokens.utils.currency.CurrencyAdditionHandler;
import com.bigbade.enchantmenttokens.utils.players.PlayerHandler;

import javax.annotation.Nonnull;
import java.util.Locale;

@RequiredArgsConstructor
public class PayGemsCmd implements CommandExecutor {
    private final PlayerHandler handler;
    private final long minTokens;

    @Override
    public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        Locale locale = CommandUtils.getLocale(commandSender, handler);
        if (args.length != 2) {
            commandSender.sendMessage(LocaleMessages.COMMAND_PAY_USAGE.translate(locale));
            return true;
        }
        if (commandSender instanceof Player) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                addGems(args[1], target, commandSender);
            }
        }
        return true;
    }

    private void addGems(String gems, Player target, CommandSender sender) {
        Locale senderLocale = CommandUtils.getLocale(sender, handler);
        try {
            long gemsLong = Long.parseLong(gems);
            EnchantmentPlayer player = handler.getPlayer((Player) sender);
            if (gemsLong <= minTokens) {
                sender.sendMessage(LocaleMessages.COMMAND_PAY_NOT_ENOUGH.translate(senderLocale,
                        LocaleMessages.translatePrice(senderLocale, minTokens)));
                return;
            }
            Locale targetLocale = handler.getPlayer(target).getLanguage();
            sender.sendMessage(LocaleMessages.COMMAND_PAY.translate(senderLocale,
                    LocaleMessages.translatePrice(senderLocale, gemsLong), target.getName()));
            target.sendMessage(LocaleMessages.COMMAND_PAY_RECEIVE.translate(targetLocale,
                    LocaleMessages.translatePrice(targetLocale,  gemsLong), sender.getName()));
            CurrencyAdditionHandler.addGems(handler.getPlayer(target), gemsLong);

            player.addGems(-gemsLong);
        } catch (NumberFormatException e) {
            sender.sendMessage(LocaleMessages.COMMAND_ERROR_NOT_NUMBER.translate(senderLocale, gems));
        }
    }
}
