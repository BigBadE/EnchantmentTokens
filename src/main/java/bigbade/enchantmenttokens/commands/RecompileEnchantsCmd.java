package bigbade.enchantmenttokens.commands;

import bigbade.enchantmenttokens.EnchantmentTokens;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashSet;

public class RecompileEnchantsCmd implements CommandExecutor {
    private EnchantmentTokens main;

    public RecompileEnchantsCmd(EnchantmentTokens main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        main.unregisterEnchants();
        main.enchants = new HashSet<>();
        main.enchants.addAll(main.loader.getEnchantments());
        main.registerEnchants();
        return true;
    }
}
