/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.localization.TranslatedStringMessage;
import software.bigbade.enchantmenttokens.utils.currency.CurrencyAdditionHandler;
import software.bigbade.enchantmenttokens.utils.players.PlayerHandler;

import javax.annotation.Nonnull;
import java.util.Locale;

public class AddGemCmd implements CommandExecutor {
    private final PlayerHandler handler;

    public AddGemCmd(PlayerHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (!sender.hasPermission(StringUtils.PERMISSION_ADMIN) && !sender.isOp()) {
            Locale locale = CommandUtils.getLocale(sender, handler);
            sender.sendMessage(new TranslatedStringMessage(locale, StringUtils.COMMAND_ERROR_PERMISSION).translate());
            return true;
        }

        if (args.length == 1) {
            if (sender instanceof Player)
                addGems(args[0], (Player) sender, sender);
        } else if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                addGems(args[1], target, sender);
            } else {
                Locale locale = CommandUtils.getLocale(sender, handler);
                sender.sendMessage(new TranslatedStringMessage(locale, StringUtils.COMMAND_ERROR_NO_PLAYER).translate(args[0]));
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
                target.sendMessage(new TranslatedStringMessage(player.getLanguage(), StringUtils.COMMAND_PAY_RECEIVE).translate(gems, sender.getName()));
            }
        } catch (NumberFormatException e) {
            Locale locale = CommandUtils.getLocale(sender, handler);
            sender.sendMessage(new TranslatedStringMessage(locale, StringUtils.COMMAND_ERROR_NOT_NUMBER).translate(gems));
        }
    }
}
