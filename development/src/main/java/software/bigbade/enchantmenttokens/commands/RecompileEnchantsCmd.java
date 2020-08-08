/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.commands;

import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.localization.TranslatedStringMessage;

import javax.annotation.Nonnull;
import java.util.Locale;

@RequiredArgsConstructor
public class RecompileEnchantsCmd implements CommandExecutor {
    private final EnchantmentTokens main;

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        Locale locale = CommandUtils.getLocale(sender, main.getPlayerHandler());
        if (!sender.hasPermission(StringUtils.PERMISSION_ADMIN) && !sender.isOp()) {
            sender.sendMessage(ChatColor.stripColor(new TranslatedStringMessage(locale, StringUtils.COMMAND_ERROR_PERMISSION).translate()));
        } else {
            main.unregisterEnchants();
            main.registerEnchants();
        }
        return true;
    }
}
