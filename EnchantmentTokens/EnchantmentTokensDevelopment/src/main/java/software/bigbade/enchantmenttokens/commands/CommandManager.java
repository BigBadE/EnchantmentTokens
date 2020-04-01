/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.commands.tabcompleter.EnchantTabCompleter;
import software.bigbade.enchantmenttokens.commands.tabcompleter.GemsTabCompleter;
import software.bigbade.enchantmenttokens.commands.tabcompleter.GenericTabCompleter;
import software.bigbade.enchantmenttokens.commands.tabcompleter.IEnchantTabCompleter;
import software.bigbade.enchantmenttokens.configuration.ConfigurationType;
import software.bigbade.enchantmenttokens.utils.BrigadierManager;

import java.util.Objects;

public class CommandManager {
    //Private constructor to hide implicit public one.
    private CommandManager() {
    }

    public static void registerCommands(EnchantmentTokens main) {
        EnchantMenuCmd menuCmd = new EnchantMenuCmd(main.getMenuFactory(), main.getPlayerHandler());
        registerCommand(main, "adminenchant", new EnchantTabCompleter(main.getEnchantmentHandler(), main.getPlayerHandler()), new EnchantCmd(main.getEnchantmentHandler(), main.getPlayerHandler()));
        registerCommand(main, "addgems", new GemsTabCompleter(true, main.getPlayerHandler()), new AddGemCmd(main.getPlayerHandler()));
        registerCommand(main, "tokenenchant", new GenericTabCompleter(0, null, main.getPlayerHandler()), menuCmd);
        registerCommand(main, "gembalance", new GenericTabCompleter(0, null, main.getPlayerHandler()), new BalanceCmd(main.getPlayerHandler()));
        registerCommand(main, "enchantlist", new GenericTabCompleter(0, StringUtils.PERMISSION_LIST, main.getPlayerHandler()), new EnchantmentListCmd(main.getEnchantmentHandler(), main.getPlayerHandler()));
        registerCommand(main, "reloadenchants", new GenericTabCompleter(0, StringUtils.PERMISSION_ADMIN, main.getPlayerHandler()), new RecompileEnchantsCmd(main));
        registerCommand(main, "paygems", new GemsTabCompleter(false, main.getPlayerHandler()), new PayGemsCmd(main.getPlayerHandler(), new ConfigurationType<>(1).getValue("minimumPay", main.getConfig())));
    }

    private static void registerCommand(EnchantmentTokens main, String name, TabCompleter tabCompleter, CommandExecutor executor) {
        Objects.requireNonNull(main.getCommand(name)).setExecutor(executor);
        if (main.getVersion() >= 13) {
            Objects.requireNonNull(main.getCommand(name)).setTabCompleter(tabCompleter);
            BrigadierManager.register(main, Objects.requireNonNull(main.getCommand(name)), ((IEnchantTabCompleter) tabCompleter).getPermission());
        }
    }
}
