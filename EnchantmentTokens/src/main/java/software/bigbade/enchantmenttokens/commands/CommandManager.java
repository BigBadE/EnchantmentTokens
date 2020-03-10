package software.bigbade.enchantmenttokens.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.utils.BrigadierManager;

import java.util.Objects;

public class CommandManager {
    //Private constructor to hide implicit public one.
    private CommandManager() {
    }

    public static void registerCommands(EnchantmentTokens main) {
        EnchantMenuCmd menuCmd = new EnchantMenuCmd(main.getMenuFactory());
        registerCommand(main, "adminenchant", new EnchantTabCompleter(main.getEnchantmentHandler()), new EnchantCmd(main.getEnchantmentHandler()));
        registerCommand(main, "addgems", new GemsTabCompleter(true), new AddGemCmd(main.getPlayerHandler()));
        registerCommand(main, "tokenenchant", new GenericTabCompleter(0, null), menuCmd);
        registerCommand(main, "gembalance", new GenericTabCompleter(0, null), new BalanceCmd(main.getPlayerHandler()));
        registerCommand(main, "enchantlist", new GenericTabCompleter(0, "enchanttoken.list"), new EnchantmentListCommand(main.getEnchantmentHandler()));
        registerCommand(main, "reloadenchants", new GenericTabCompleter(0, "enchanttoken.admin"), new RecompileEnchantsCmd(main));
        registerCommand(main, "paygems", new GemsTabCompleter(false), new PayGemsCmd(main.getPlayerHandler()));
    }

    private static void registerCommand(EnchantmentTokens main, String name, TabCompleter tabCompleter, CommandExecutor executor) {
        Objects.requireNonNull(main.getCommand(name)).setExecutor(executor);
        if (main.getVersion() >= 13) {
            Objects.requireNonNull(main.getCommand(name)).setTabCompleter(tabCompleter);
            BrigadierManager.register(main, Objects.requireNonNull(main.getCommand(name)), ((IEnchantTabCompleter) tabCompleter).getPermission());
        }
    }
}
