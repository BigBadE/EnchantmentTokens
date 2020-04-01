/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.commands;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.gui.EnchantmentGUI;
import software.bigbade.enchantmenttokens.gui.MenuFactory;
import software.bigbade.enchantmenttokens.localization.TranslatedStringMessage;
import software.bigbade.enchantmenttokens.utils.players.PlayerHandler;

import javax.annotation.Nonnull;

@RequiredArgsConstructor
public class EnchantMenuCmd implements CommandExecutor {
    private final MenuFactory factory;
    private final PlayerHandler handler;

    @Override
    public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (commandSender instanceof Player) {
            EnchantmentGUI inv = factory.genInventory((Player) commandSender);
            if (inv == null)
                commandSender.sendMessage(new TranslatedStringMessage(handler.getPlayer((Player) commandSender).getLanguage(), StringUtils.COMMAND_ENCHANT_HELD).translate());
        }
        return true;
    }
}
