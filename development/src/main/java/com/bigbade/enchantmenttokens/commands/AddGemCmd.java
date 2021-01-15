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

public class AddGemCmd implements CommandExecutor {
    private final PlayerHandler handler;

    public AddGemCmd(PlayerHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (!sender.hasPermission("enchanttoken.admin") && !sender.isOp()) {
            Locale locale = CommandUtils.getLocale(sender, handler);
            sender.sendMessage(LocaleMessages.COMMAND_ERROR_PERMISSION.translate(locale));
            return true;
        }

        if (args.length == 1) {
            if (sender instanceof Player) {
                addGems(args[0], (Player) sender, sender);
            }
        } else if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                addGems(args[1], target, sender);
            } else {
                Locale locale = CommandUtils.getLocale(sender, handler);
                sender.sendMessage(LocaleMessages.COMMAND_ERROR_NO_PLAYER.translate(locale, args[0]));
            }
        }
        return true;
    }

    private void addGems(String gems, Player target, CommandSender sender) {
        try {
            EnchantmentPlayer player = handler.getPlayer(target);
            long gemsLong = Long.parseLong(gems);
            CurrencyAdditionHandler.addGems(player, gemsLong);
            if (gemsLong > 0) {
                target.sendMessage(LocaleMessages.COMMAND_PAY_RECEIVE
                        .translate(player.getLanguage(), gems, sender.getName()));
            }
        } catch (NumberFormatException e) {
            Locale locale = CommandUtils.getLocale(sender, handler);
            sender.sendMessage(LocaleMessages.COMMAND_ERROR_NOT_NUMBER.translate(locale, gems));
        }
    }
}
