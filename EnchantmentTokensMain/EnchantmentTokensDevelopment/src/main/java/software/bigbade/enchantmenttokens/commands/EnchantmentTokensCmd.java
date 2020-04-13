/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.commands;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.localization.TranslatedStringMessage;
import software.bigbade.enchantmenttokens.utils.players.PlayerHandler;

import javax.annotation.Nonnull;
import java.util.Locale;

@RequiredArgsConstructor
public class EnchantmentTokensCmd implements CommandExecutor {
    private final PlayerHandler playerHandler;

    @Override
    public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (args.length == 0 && commandSender instanceof Player) {
            commandSender.sendMessage(translateString((Player) commandSender, StringUtils.COMMAND_ENCHANTMENT_TOKENS_USAGE));
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("language") && commandSender instanceof Player) {
                commandSender.sendMessage(translateString((Player) commandSender, StringUtils.COMMAND_ENCHANTMENT_TOKENS_LANGUAGES));
            } else if (args[0].equalsIgnoreCase("version")) {
                commandSender.sendMessage("EnchantmentTokens v1.0");
            }
        } else if (args.length == 2) {
            if (commandSender instanceof Player) {
                playerHandler.getPlayer((Player) commandSender).setLanguage(getLocale(args[1]));
            } else if (commandSender.isOp()) {
                Locale.setDefault(getLocale(args[1]));
            }
        }
        return true;
    }

    private Locale getLocale(String name) {
        Locale locale = Locale.getDefault();
        for (Locale found : Locale.getAvailableLocales()) {
            if (found.getCountry().equalsIgnoreCase(name)) {
                locale = found;
                break;
            }
        }
        return locale;
    }

    private String translateString(Player player, String string) {
        return new TranslatedStringMessage(playerHandler.getPlayer(player).getLanguage(), string).translate();
    }
}
