/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.commands;

import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.api.VanillaEnchant;
import software.bigbade.enchantmenttokens.localization.TranslatedStringMessage;
import software.bigbade.enchantmenttokens.utils.RomanNumeralConverter;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import software.bigbade.enchantmenttokens.utils.players.PlayerHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
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
        int level;

        try {
            level = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(new TranslatedStringMessage(locale, StringUtils.COMMAND_ENCHANT_USAGE).translate());
            return true;
        }

        for (EnchantmentBase enchantment : handler.getAllEnchants()) {
            if (enchantment instanceof VanillaEnchant)
                continue;
            if (enchantment.getKey().toString().equals(args[0])) {
                addEnchant((Player) sender, item, enchantment, level, locale);
                return true;
            }
        }
        sender.sendMessage(new TranslatedStringMessage(locale, StringUtils.COMMAND_ERROR_NO_ENCHANTMENT).translate());
        return true;
    }

    private void addEnchant(Player player, ItemStack item, EnchantmentBase base, int level, Locale locale) {
        item.addEnchantment(base.getEnchantment(), level);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        List<String> lore = meta.getLore();
        if (lore == null)
            lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + base.getEnchantmentName() + ": " + RomanNumeralConverter.getRomanNumeral(level));
        meta.setLore(lore);
        item.setItemMeta(meta);
        player.sendMessage(new TranslatedStringMessage(locale, StringUtils.COMMAND_ADD).translate(base.getEnchantmentName()));
    }
}
