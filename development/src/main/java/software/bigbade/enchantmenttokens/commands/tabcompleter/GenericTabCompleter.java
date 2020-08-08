/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.commands.tabcompleter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.commands.CommandUtils;
import software.bigbade.enchantmenttokens.localization.TranslatedStringMessage;
import software.bigbade.enchantmenttokens.utils.players.PlayerHandler;

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
    public List<String> onTabComplete(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        Locale locale = CommandUtils.getLocale(commandSender, handler);
        if (permission != null && !commandSender.hasPermission(permission) && !commandSender.isOp()) {
            return Collections.singletonList(ChatColor.stripColor(new TranslatedStringMessage(locale, StringUtils.COMMAND_ERROR_PERMISSION).translate()));
        }
        if (args.length > this.args) {
            return Collections.singletonList(ChatColor.stripColor(new TranslatedStringMessage(locale, StringUtils.COMMAND_ERROR_TOO_MANY_ARGUMENTS).translate()));
        }
        return Collections.emptyList();
    }
}
