/*
 * Custom enchantments for Minecraft
 * Copyright (C) 2021 Big_Bad_E
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bigbade.enchantmenttokens.commands;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import com.bigbade.enchantmenttokens.api.EnchantmentBase;
import com.bigbade.enchantmenttokens.api.VanillaEnchant;
import com.bigbade.enchantmenttokens.localization.LocaleMessages;
import com.bigbade.enchantmenttokens.utils.enchants.EnchantUtils;
import com.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import com.bigbade.enchantmenttokens.utils.players.PlayerHandler;

import javax.annotation.Nonnull;
import java.util.Locale;

@RequiredArgsConstructor
public class EnchantCmd implements CommandExecutor {
    private final EnchantmentHandler handler;
    private final PlayerHandler playerHandler;

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        Locale locale = CommandUtils.getLocale(sender, playerHandler);
        if (!sender.hasPermission("enchanttoken.admin") && !sender.isOp()) {
            sender.sendMessage(LocaleMessages.COMMAND_ERROR_PERMISSION.translate(locale));
            return true;
        }
        if (args.length != 2 || !(sender instanceof Player)) {
            sender.sendMessage(LocaleMessages.COMMAND_ENCHANT_USAGE.translate(locale));
            return true;
        }

        try {
            addEnchantment(sender, locale, args[0], Integer.parseInt(args[1]));
        } catch (NumberFormatException e) {
            sender.sendMessage(LocaleMessages.COMMAND_ENCHANT_USAGE.translate(locale));
            return true;
        }


        return true;
    }

    private void addEnchantment(CommandSender sender, Locale locale, String input, int level) {
        for (EnchantmentBase enchantment : handler.getAllEnchants()) {
            if (enchantment instanceof VanillaEnchant) {
                continue;
            }
            if (enchantment.getKey().toString().startsWith(input)
                    || enchantment.getEnchantmentName().toLowerCase().startsWith(input.toLowerCase())) {
                if (level < enchantment.getStartLevel() || level > enchantment.getMaxLevel()) {
                    sender.sendMessage(LocaleMessages.COMMAND_ERROR_BAD_LEVEL.translate(locale, input));
                    return;
                }
                ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
                EnchantUtils.getInstance().addEnchantmentBaseNoMessages(item, enchantment,
                        (Player) sender, level - 1);
                sender.sendMessage(LocaleMessages.COMMAND_ADD.translate(locale, enchantment.getEnchantmentName()));
                return;
            }
        }
        sender.sendMessage(LocaleMessages.COMMAND_ERROR_NO_ENCHANTMENT.translate(locale, input));
    }
}
