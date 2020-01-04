package bigbade.enchantmenttokens.commands;

import bigbade.enchantmenttokens.EnchantmentTokens;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCmd implements CommandExecutor {
    private EnchantmentTokens main;

    public BalanceCmd(EnchantmentTokens main) {
        this.main = main;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player)
            commandSender.sendMessage(ChatColor.GREEN + "Your balance: " + main.fileLoader.loadPlayer((Player) commandSender).getGems() + "G");
        return true;
    }
}
