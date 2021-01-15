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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.bigbade.enchantmenttokens.localization.LocaleMessages;
import com.bigbade.enchantmenttokens.utils.players.PlayerHandler;

import javax.annotation.Nonnull;
import java.util.Locale;

@RequiredArgsConstructor
public class EnchantmentTokensCmd implements CommandExecutor {
    private final PlayerHandler playerHandler;

    @Override
    public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command,
                             @Nonnull String label, @Nonnull String[] args) {
        if (args.length == 0 && commandSender instanceof Player) {
            commandSender.sendMessage(translateString((Player) commandSender,
                    LocaleMessages.COMMAND_ENCHANTMENT_TOKENS_USAGE));
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("language") && commandSender instanceof Player) {
                commandSender.sendMessage(translateString((Player) commandSender,
                        LocaleMessages.COMMAND_ENCHANTMENT_TOKENS_LANGUAGES));
            } else if (args[0].equalsIgnoreCase("version")) {
                commandSender.sendMessage("EnchantmentTokens v1.0.0-Alpha");
            }
        } else if (args.length == 2) {
            if (commandSender instanceof Player) {
                playerHandler.getPlayer((Player) commandSender).setLanguage(EnchantmentTokensCmd.getLocale(args[1]));
            } else if (commandSender.isOp()) {
                Locale.setDefault(EnchantmentTokensCmd.getLocale(args[1]));
            }
        }
        return true;
    }

    private static Locale getLocale(String name) {
        Locale locale = Locale.getDefault();
        for (Locale found : Locale.getAvailableLocales()) {
            if (found.getCountry().equalsIgnoreCase(name)) {
                locale = found;
                break;
            }
        }
        return locale;
    }

    private String translateString(Player player, LocaleMessages message) {
        return message.translate(playerHandler.getPlayer(player).getLanguage());
    }
}
