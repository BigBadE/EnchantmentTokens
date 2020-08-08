/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.commands.tabcompleter;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public class GemsTabCompleter implements TabCompleter, IEnchantTabCompleter {
    private final boolean admin;
    private final PlayerHandler playerHandler;

    @Nullable
    @Override
    public List<String> onTabComplete(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        Locale locale = CommandUtils.getLocale(commandSender, playerHandler);
        if (admin && !commandSender.hasPermission("enchanttoken.admin") && !commandSender.isOp())
            return Collections.singletonList(ChatColor.stripColor(new TranslatedStringMessage(locale, StringUtils.COMMAND_ERROR_PERMISSION).translate()));
        if (args.length > 2)
            return Collections.singletonList(ChatColor.stripColor(new TranslatedStringMessage(locale, StringUtils.COMMAND_ERROR_TOO_MANY_ARGUMENTS).translate()));
        if (args.length == 1) {
            //Return player names
            List<String> players = new ArrayList<>();
            commandSender.getServer().getOnlinePlayers().forEach(player -> {
                if (player.getName().startsWith(args[0])) players.add(player.getName());
            });
            if (players.isEmpty() && !checkLong(args[0], locale).isEmpty())
                players.add(ChatColor.stripColor(new TranslatedStringMessage(locale, StringUtils.COMMAND_ERROR_NO_PLAYER).translate(args[0])));
            return players;
        } else {
            if(args[1].isEmpty()) {
                return Collections.emptyList();
            }
            return checkLong(args[1], locale);
        }
    }

    private List<String> checkLong(String number, Locale locale) {
        try {
            Long.parseLong(number);
            return Collections.emptyList();
        } catch (NumberFormatException e) {
            return Collections.singletonList(ChatColor.stripColor(new TranslatedStringMessage(locale, StringUtils.COMMAND_ERROR_NOT_NUMBER).translate(number)));
        }
    }

    @Override
    public String getPermission() {
        return admin ? "enchanttoken.admin" : null;
    }
}