package software.bigbade.enchantmenttokens.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import software.bigbade.enchantmenttokens.EnchantmentTokens;

import java.util.Objects;

public class CommandManager {
    private EnchantmentTokens main;

    public CommandManager(EnchantmentTokens main) {
        this.main = main;

        EnchantMenuCmd menuCmd = new EnchantMenuCmd(main.getMenuFactory());
        registerCommand("adminenchant", new EnchantTabCompleter(main.getEnchantmentHandler()), new EnchantCmd(main.getEnchantmentHandler(), main.getUtils()));
        registerCommand("addgems", new AddGemTabCompleter(), new AddGemCmd(main.getPlayerHandler()));
        registerCommand("tokenenchant", new GenericTabCompleter(0), menuCmd);
        registerCommand("enchantlist", new GenericTabCompleter(0), new BalanceCmd(main.getPlayerHandler()));
        registerCommand("reloadenchants", new GenericTabCompleter(0), new EnchantmentListCommand(main.getEnchantmentHandler()));
        registerCommand("paygems", new GenericTabCompleter(0), new RecompileEnchantsCmd(main));
    }

    private void registerCommand(String name, TabCompleter tabCompleter, CommandExecutor executor) {
        Objects.requireNonNull(main.getCommand(name)).setExecutor(executor);
        Objects.requireNonNull(main.getCommand(name)).setTabCompleter(tabCompleter);
    }
}
