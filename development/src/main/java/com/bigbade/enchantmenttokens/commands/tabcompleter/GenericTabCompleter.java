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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import com.bigbade.enchantmenttokens.commands.CommandUtils;
import com.bigbade.enchantmenttokens.localization.LocaleMessages;
import com.bigbade.enchantmenttokens.utils.players.PlayerHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public class GenericTabCompleter implements TabCompleter, IEnchantTabCompleter {
    private final int args;
    @Nullable
    @Getter
    private final String permission;
    private final PlayerHandler handler;

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender commandSender, @Nonnull Command command,
                                      @Nonnull String label, @Nonnull String[] args) {
        Locale locale = CommandUtils.getLocale(commandSender, handler);
        if (permission != null && !commandSender.hasPermission(permission) && !commandSender.isOp()) {
            return Collections.singletonList(ChatColor.stripColor(
                    LocaleMessages.COMMAND_ERROR_PERMISSION.translate(locale)));
        }
        if (args.length > this.args) {
            return Collections.singletonList(ChatColor.stripColor(
                    LocaleMessages.COMMAND_ERROR_TOO_MANY_ARGUMENTS.translate(locale)));
        }
        return Collections.emptyList();
    }
}
