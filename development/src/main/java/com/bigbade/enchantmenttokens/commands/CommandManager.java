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

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import com.bigbade.enchantmenttokens.EnchantmentTokens;
import com.bigbade.enchantmenttokens.commands.tabcompleter.EnchantTabCompleter;
import com.bigbade.enchantmenttokens.commands.tabcompleter.GemsTabCompleter;
import com.bigbade.enchantmenttokens.commands.tabcompleter.GenericTabCompleter;
import com.bigbade.enchantmenttokens.commands.tabcompleter.IEnchantTabCompleter;
import com.bigbade.enchantmenttokens.configuration.ConfigurationType;
import com.bigbade.enchantmenttokens.utils.BrigadierManager;
import com.bigbade.enchantmenttokens.utils.ReflectionManager;

import java.util.Objects;

public final class CommandManager {
    //Private constructor to hide implicit public one.
    private CommandManager() {
    }

    public static void registerCommands(EnchantmentTokens main) {
        EnchantMenuCmd menuCmd = new EnchantMenuCmd(main.getMenuFactory(), main.getPlayerHandler());
        registerCommand(main, "enchantmenttokens", null, new EnchantmentTokensCmd(main.getPlayerHandler()));
        registerCommand(main, "adminenchant", new EnchantTabCompleter(main.getEnchantmentHandler(), main.getPlayerHandler()), new EnchantCmd(main.getEnchantmentHandler(), main.getPlayerHandler()));
        registerCommand(main, "addgems", new GemsTabCompleter(true, main.getPlayerHandler()), new AddGemCmd(main.getPlayerHandler()));
        registerCommand(main, "tokenenchant", new GenericTabCompleter(0, null, main.getPlayerHandler()), menuCmd);
        registerCommand(main, "gembalance", new GenericTabCompleter(0, null, main.getPlayerHandler()), new BalanceCmd(main.getPlayerHandler()));
        registerCommand(main, "enchantlist", new GenericTabCompleter(0, "enchanttoken.list", main.getPlayerHandler()), new EnchantmentListCmd(main.getEnchantmentHandler(), main.getPlayerHandler()));
        registerCommand(main, "reloadenchants", new GenericTabCompleter(0, "enchanttoken.admin", main.getPlayerHandler()), new RecompileEnchantsCmd(main));
        registerCommand(main, "paygems", new GemsTabCompleter(false, main.getPlayerHandler()), new PayGemsCmd(main.getPlayerHandler(), new ConfigurationType<>(1).getValue("minimumPay", main.getConfig())));
    }

    private static void registerCommand(EnchantmentTokens main, String name, TabCompleter tabCompleter, CommandExecutor executor) {
        Objects.requireNonNull(main.getCommand(name)).setExecutor(executor);
        if (ReflectionManager.VERSION >= 13 && tabCompleter != null) {
            Objects.requireNonNull(main.getCommand(name)).setTabCompleter(tabCompleter);
            BrigadierManager.register(main, Objects.requireNonNull(main.getCommand(name)), ((IEnchantTabCompleter) tabCompleter).getPermission());
        }
    }
}
