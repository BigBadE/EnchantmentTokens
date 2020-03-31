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
import software.bigbade.enchantmenttokens.localization.TranslatedPriceMessage;
import software.bigbade.enchantmenttokens.utils.currency.CurrencyAdditionHandler;
import software.bigbade.enchantmenttokens.utils.players.PlayerHandler;

import javax.annotation.Nonnull;

public class PayGemsCmd implements CommandExecutor {
    private PlayerHandler handler;
    private long minTokens;

    public PayGemsCmd(PlayerHandler handler, long minTokens) {
        this.handler = handler;
        this.minTokens = minTokens;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if(args.length != 2) {
            commandSender.sendMessage(StringUtils.COMMAND_PAY_USAGE);
        }
        if(commandSender instanceof Player) {
            Player target = Bukkit.getPlayer(args[0]);
            if(target != null) {
                addGems(args[1], target, commandSender);
            }
        }
        return true;
    }

    private void addGems(String gems, Player target, CommandSender sender) {
        try {
            long gemsLong = Long.parseLong(gems);
            EnchantmentPlayer player = handler.getPlayer((Player) sender);
            if(gemsLong == 1) {
                sender.sendMessage(StringUtils.COMMAND_PAY_NOT_ENOUGH.translate(new TranslatedPriceMessage().translate(minTokens + "")));
                return;
            }
            sender.sendMessage(StringUtils.COMMAND_PAY.translate(new TranslatedPriceMessage().translate("" + gemsLong), target.getName()));
            target.sendMessage(StringUtils.COMMAND_PAY_RECEIVE.translate(new TranslatedPriceMessage().translate("" + gemsLong), sender.getName()));
            CurrencyAdditionHandler.addGems(handler.getPlayer(target), gemsLong);

            player.addGems(-gemsLong);
        } catch (NumberFormatException e) {
            sender.sendMessage(StringUtils.COMMAND_ERROR_NOT_NUMBER.translate(gems));
        }
    }
}
