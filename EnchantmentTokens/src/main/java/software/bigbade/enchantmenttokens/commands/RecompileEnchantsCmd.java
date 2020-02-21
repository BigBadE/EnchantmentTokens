package software.bigbade.enchantmenttokens.commands;

import software.bigbade.enchantmenttokens.EnchantmentTokens;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RecompileEnchantsCmd implements CommandExecutor {
    private EnchantmentTokens main;

    public RecompileEnchantsCmd(EnchantmentTokens main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("enchanttoken.admin") && !sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to run this command");
            return true;
        }
        main.unregisterEnchants();
        main.registerEnchants();
        return true;
    }
}
