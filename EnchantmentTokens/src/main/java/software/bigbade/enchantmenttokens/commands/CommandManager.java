package software.bigbade.enchantmenttokens.commands;

import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.commands.*;

import java.util.Objects;

public class CommandManager {
    public CommandManager(EnchantmentTokens main) {
        Objects.requireNonNull(main.getCommand("adminenchant")).setExecutor(new EnchantCmd(main.getEnchantmentHandler(), main.getUtils()));
        Objects.requireNonNull(main.getCommand("adminenchant")).setTabCompleter(new EnchantTabCompleter(main.getEnchantmentHandler()));

        Objects.requireNonNull(main.getCommand("addgems")).setExecutor(new AddGemCmd(main.getPlayerHandler()));
        Objects.requireNonNull(main.getCommand("addgems")).setTabCompleter(new AddGemTabCompleter());

        EnchantMenuCmd menuCmd = new EnchantMenuCmd(main.getMenuFactory());
        Objects.requireNonNull(main.getCommand("tokenenchant")).setExecutor(menuCmd);
        Objects.requireNonNull(main.getCommand("tokenenchant")).setTabCompleter(new GenericTabCompleter(0));

        Objects.requireNonNull(main.getCommand("gembal")).setExecutor(new BalanceCmd(main.getPlayerHandler()));
        Objects.requireNonNull(main.getCommand("gembal")).setTabCompleter(new GenericTabCompleter(0));

        Objects.requireNonNull(main.getCommand("enchantlist")).setExecutor(new EnchantmentListCommand(main.getEnchantmentHandler()));
        Objects.requireNonNull(main.getCommand("enchantlist")).setTabCompleter(new GenericTabCompleter(0));

        Objects.requireNonNull(main.getCommand("reloadenchants")).setExecutor(new RecompileEnchantsCmd(main));
        Objects.requireNonNull(main.getCommand("reloadenchants")).setTabCompleter(new GenericTabCompleter(0));
    }
}
