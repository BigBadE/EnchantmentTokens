/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.commands;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.localization.TranslatedStringMessage;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import software.bigbade.enchantmenttokens.utils.players.PlayerHandler;

import javax.annotation.Nonnull;
import java.util.Locale;

@RequiredArgsConstructor
public class EnchantmentListCmd implements CommandExecutor {
    private final EnchantmentHandler handler;
    private final PlayerHandler playerHandler;

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        Locale locale = CommandUtils.getLocale(sender, playerHandler);
        if (!sender.hasPermission(StringUtils.PERMISSION_LIST) && !sender.isOp()) {
            sender.sendMessage(new TranslatedStringMessage(locale, StringUtils.COMMAND_ERROR_PERMISSION).translate());
        } else {
            StringBuilder builder = new StringBuilder();
            handler.getAllEnchants().forEach(enchant -> builder.append(enchant.getEnchantmentName()).append(", "));
            if (builder.length() > 0)
                builder.setLength(builder.length() - 2);
            sender.sendMessage(new TranslatedStringMessage(locale, StringUtils.COMMAND_LIST).translate(builder.toString()));
        }
        return true;
    }
}
