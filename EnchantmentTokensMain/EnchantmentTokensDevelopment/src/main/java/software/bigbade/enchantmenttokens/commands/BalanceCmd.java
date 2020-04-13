/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.commands;

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

public class BalanceCmd implements CommandExecutor {
    private final PlayerHandler handler;

    public BalanceCmd(PlayerHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] strings) {
        if(commandSender instanceof Player) {
            EnchantmentPlayer player = handler.getPlayer((Player) commandSender);
            commandSender.sendMessage(new TranslatedStringMessage(player.getLanguage(), StringUtils.COMMAND_BALANCE).translate(CurrencyAdditionHandler.formatMoney(player.getLanguage(), player.getGems())));
        }
        return true;
    }
}
