package bigbade.enchantmenttokens.utils.commands;

import bigbade.enchantmenttokens.EnchantmentTokens;
import bigbade.enchantmenttokens.commands.*;

import java.util.Objects;

public class CommandManager {
    public CommandManager(EnchantmentTokens main) {
        Objects.requireNonNull(main.getCommand("adminenchant")).setExecutor(new EnchantCmd(main.getEnchantmentHandler(), main.getUtils()));
        Objects.requireNonNull(main.getCommand("adminenchant")).setTabCompleter(new EnchantTabCompleter(main.getEnchantmentHandler()));

        Objects.requireNonNull(main.getCommand("addgems")).setExecutor(new AddGemCmd(main.getPlayerHandler()));
        Objects.requireNonNull(main.getCommand("addgems")).setTabCompleter(new AddGemTabCompleter());

        EnchantMenuCmd menuCmd = new EnchantMenuCmd(main.getFactory());
        Objects.requireNonNull(main.getCommand("tokenenchant")).setExecutor(menuCmd);
        Objects.requireNonNull(main.getCommand("tokenenchant")).setTabCompleter(new GenericTabCompleter());

        Objects.requireNonNull(main.getCommand("gembal")).setExecutor(new BalanceCmd(main.getPlayerHandler()));
        Objects.requireNonNull(main.getCommand("gembal")).setTabCompleter(new GenericTabCompleter());

        Objects.requireNonNull(main.getCommand("enchantlist")).setExecutor(new EnchantmentListCommand(main.getEnchantmentHandler()));
        Objects.requireNonNull(main.getCommand("enchantlist")).setTabCompleter(new GenericTabCompleter());

        Objects.requireNonNull(main.getCommand("reloadenchants")).setExecutor(new RecompileEnchantsCmd(main));
        Objects.requireNonNull(main.getCommand("reloadenchants")).setTabCompleter(new GenericTabCompleter());
    }
}
