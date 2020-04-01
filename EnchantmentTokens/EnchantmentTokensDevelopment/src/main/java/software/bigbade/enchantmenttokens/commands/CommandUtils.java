/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.utils.players.PlayerHandler;

import java.util.Locale;

public final class CommandUtils {
    //Private constructor to hide implicit public one.
    private CommandUtils() {
    }

    public static Locale getLocale(CommandSender sender, PlayerHandler playerHandler) {
        if (sender instanceof Player) {
            return playerHandler.getPlayer((Player) sender).getLanguage();
        } else {
            return Locale.getDefault();
        }
    }
}
