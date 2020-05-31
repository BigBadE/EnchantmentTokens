/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.commands;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.api.VanillaEnchant;
import software.bigbade.enchantmenttokens.localization.TranslatedStringMessage;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantUtils;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import software.bigbade.enchantmenttokens.utils.players.PlayerHandler;

import javax.annotation.Nonnull;
import java.util.Locale;

@RequiredArgsConstructor
public class EnchantCmd implements CommandExecutor {
    private final EnchantmentHandler handler;
    private final PlayerHandler playerHandler;

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        Locale locale = CommandUtils.getLocale(sender, playerHandler);
        if (!sender.hasPermission(StringUtils.PERMISSION_ADMIN) && !sender.isOp()) {
            sender.sendMessage(new TranslatedStringMessage(locale, StringUtils.COMMAND_ERROR_PERMISSION).translate());
            return true;
        }
        if (args.length != 2 || !(sender instanceof Player)) {
            sender.sendMessage(new TranslatedStringMessage(locale, StringUtils.COMMAND_ENCHANT_USAGE).translate());
            return true;
        }

        ItemStack item = ((Player) sender).getInventory().getItemInMainHand();

        try {
            Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(new TranslatedStringMessage(locale, StringUtils.COMMAND_ENCHANT_USAGE).translate());
            return true;
        }

        for (EnchantmentBase enchantment : handler.getAllEnchants()) {
            if (enchantment instanceof VanillaEnchant)
                continue;
            if (enchantment.getKey().toString().startsWith(args[0]) || enchantment.getEnchantmentName().startsWith(args[0])) {
                EnchantUtils.getInstance().addEnchantmentBaseNoMessages(item, enchantment, (Player) sender);
                sender.sendMessage(new TranslatedStringMessage(locale, StringUtils.COMMAND_ADD).translate(enchantment.getEnchantmentName()));
                return true;
            }
        }
        sender.sendMessage(new TranslatedStringMessage(locale, StringUtils.COMMAND_ERROR_NO_ENCHANTMENT).translate(args[0]));
        return true;
    }
}
