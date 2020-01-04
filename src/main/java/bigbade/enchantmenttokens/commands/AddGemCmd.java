package bigbade.enchantmenttokens.commands;

import bigbade.enchantmenttokens.EnchantmentTokens;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.InputStream;

public class AddGemCmd implements CommandExecutor {
    private EnchantmentTokens main;

    public AddGemCmd(EnchantmentTokens main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (sender instanceof Player)
                try {
                    main.fileLoader.loadPlayer((Player) sender).addGems(Integer.parseInt(args[0]));
                    sender.sendMessage(ChatColor.GREEN + "Added " + args[0] + "G!");
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + args[0] + " is not an Integer!");
                }
        } else if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                try {
                    main.fileLoader.loadPlayer(target).addGems(Integer.parseInt(args[1]));
                    sender.sendMessage(ChatColor.GREEN + "Added " + args[1] + "G!");
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + args[1] + " is not an Integer!");
                }
            } else {
                sender.sendMessage(ChatColor.RED + args[0] + " is not a Player!");
            }
        }
        return true;
    }
}
