package software.bigbade.enchantmenttokens.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import software.bigbade.enchantmenttokens.EnchantmentTokens;

import java.util.Objects;

public class CommandManager {
    public static void registerCommands(EnchantmentTokens main) {
        EnchantMenuCmd menuCmd = new EnchantMenuCmd(main.getMenuFactory());
        registerCommand(main, "adminenchant", new EnchantTabCompleter(main.getEnchantmentHandler()), new EnchantCmd(main.getEnchantmentHandler(), main.getUtils()));
        registerCommand(main, "addgems", new AddGemTabCompleter(), new AddGemCmd(main.getPlayerHandler()));
        registerCommand(main, "tokenenchant", new GenericTabCompleter(0), menuCmd);
        registerCommand(main, "enchantlist", new GenericTabCompleter(0), new BalanceCmd(main.getPlayerHandler()));
        registerCommand(main, "reloadenchants", new GenericTabCompleter(0), new EnchantmentListCommand(main.getEnchantmentHandler()));
        registerCommand(main, "paygems", new GenericTabCompleter(0), new RecompileEnchantsCmd(main));
    }

    private static void registerCommand(EnchantmentTokens main, String name, TabCompleter tabCompleter, CommandExecutor executor) {
        Objects.requireNonNull(main.getCommand(name)).setExecutor(executor);
        Objects.requireNonNull(main.getCommand(name)).setTabCompleter(tabCompleter);
    }
}
