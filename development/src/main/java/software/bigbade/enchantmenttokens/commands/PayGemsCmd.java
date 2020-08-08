/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.commands;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.localization.TranslatedPriceMessage;
import software.bigbade.enchantmenttokens.localization.TranslatedStringMessage;
import software.bigbade.enchantmenttokens.utils.currency.CurrencyAdditionHandler;
import software.bigbade.enchantmenttokens.utils.players.PlayerHandler;

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
            commandSender.sendMessage(new TranslatedStringMessage(locale, StringUtils.COMMAND_PAY_USAGE).translate());
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
                sender.sendMessage(new TranslatedStringMessage(senderLocale, StringUtils.COMMAND_PAY_NOT_ENOUGH).translate(new TranslatedPriceMessage(senderLocale).translate(minTokens + "")));
                return;
            }
            Locale targetLocale = handler.getPlayer(target).getLanguage();
            sender.sendMessage(new TranslatedStringMessage(senderLocale, StringUtils.COMMAND_PAY).translate(new TranslatedPriceMessage(senderLocale).translate("" + gemsLong), target.getName()));
            target.sendMessage(new TranslatedStringMessage(targetLocale, StringUtils.COMMAND_PAY_RECEIVE).translate(new TranslatedPriceMessage(targetLocale).translate("" + gemsLong), sender.getName()));
            CurrencyAdditionHandler.addGems(handler.getPlayer(target), gemsLong);

            player.addGems(-gemsLong);
        } catch (NumberFormatException e) {
            sender.sendMessage(new TranslatedStringMessage(senderLocale, StringUtils.COMMAND_ERROR_NOT_NUMBER).translate(gems));
        }
    }
}
