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
import com.bigbade.enchantmenttokens.gui.EnchantmentGUI;
import com.bigbade.enchantmenttokens.gui.EnchantmentMenuFactory;
import com.bigbade.enchantmenttokens.localization.LocaleMessages;
import com.bigbade.enchantmenttokens.utils.players.PlayerHandler;

import javax.annotation.Nonnull;

@RequiredArgsConstructor
public class EnchantMenuCmd implements CommandExecutor {
    private final EnchantmentMenuFactory factory;
    private final PlayerHandler handler;

    @Override
    public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command,
                             @Nonnull String label, @Nonnull String[] args) {
        if (commandSender instanceof Player) {
            EnchantmentGUI inv = factory.genInventory((Player) commandSender);
            if (inv == null) {
                commandSender.sendMessage(LocaleMessages.COMMAND_ENCHANT_HELD
                        .translate(handler.getPlayer((Player) commandSender).getLanguage()));
            }
        }
        return true;
    }
}
