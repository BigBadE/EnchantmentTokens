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

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import com.bigbade.enchantmenttokens.localization.LocaleMessages;
import com.bigbade.enchantmenttokens.utils.currency.CurrencyAdditionHandler;
import com.bigbade.enchantmenttokens.utils.players.PlayerHandler;

import javax.annotation.Nonnull;

public class BalanceCmd implements CommandExecutor {
    private final PlayerHandler handler;

    public BalanceCmd(PlayerHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] strings) {
        if(commandSender instanceof Player) {
            EnchantmentPlayer player = handler.getPlayer((Player) commandSender);
            player.getGems().thenAccept(gems -> commandSender.sendMessage(LocaleMessages.COMMAND_BALANCE
                    .translate(player.getLanguage(), CurrencyAdditionHandler.formatMoney(player.getLanguage(), gems))));
        }
        return true;
    }
}
