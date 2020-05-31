/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.commands.tabcompleter;

import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.api.VanillaEnchant;
import software.bigbade.enchantmenttokens.commands.CommandUtils;
import software.bigbade.enchantmenttokens.localization.TranslatedStringMessage;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import software.bigbade.enchantmenttokens.utils.players.PlayerHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public class EnchantTabCompleter implements TabCompleter, IEnchantTabCompleter {
    private final EnchantmentHandler handler;
    private final PlayerHandler playerHandler;

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        Locale locale = CommandUtils.getLocale(sender, playerHandler);
        if (!sender.hasPermission(StringUtils.PERMISSION_ADMIN) && !sender.isOp())
            return Collections.singletonList(ChatColor.stripColor(new TranslatedStringMessage(locale, StringUtils.COMMAND_ERROR_PERMISSION).translate()));
        if (args.length == 1) {
            return getKeys(args[0]);
        } else if (args.length == 2) {
            return checkInt(args[1], locale);
        } else {
            return Collections.singletonList(ChatColor.stripColor(new TranslatedStringMessage(locale, StringUtils.COMMAND_ERROR_TOO_MANY_ARGUMENTS).translate()));
        }
    }

    private List<String> checkInt(String number, Locale locale) {
        try {
            if (number.trim().length() > 0)
                Integer.parseInt(number);
            return Collections.emptyList();
        } catch (NumberFormatException e) {
            return Collections.singletonList(ChatColor.stripColor(new TranslatedStringMessage(locale, StringUtils.COMMAND_ERROR_NOT_NUMBER).translate(number)));
        }
    }

    public List<String> getKeys(String name) {
        List<String> suggestions = new ArrayList<>();
        for (EnchantmentBase base : handler.getAllEnchants()) {
            if(base instanceof VanillaEnchant) {
                continue;
            }
            String key = base.getKey().toString();
            if (key.startsWith(name) || base.getEnchantmentName().startsWith(name)) {
                suggestions.add(key);
                if(suggestions.size() > 10)
                    break;
            }
        }
        return suggestions;
    }

    @Override
    public String getPermission() {
        return "enchanttoken.admin";
    }
}
