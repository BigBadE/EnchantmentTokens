package bigbade.enchantmenttokens.commands;

/*
EnchantmentTokens
Copyright (C) 2019-2020 Big_Bad_E
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

import bigbade.enchantmenttokens.EnchantmentTokens;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddGemCmd implements CommandExecutor {
    private EnchantmentTokens main;

    public AddGemCmd(EnchantmentTokens main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("enchanttoken.admin") && !sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to run this command");
            return true;
        }
        if (args.length == 1) {
            if (sender instanceof Player)
                try {
                    main.getPlayerHandler().getPlayer((Player) sender, main.getCurrencyHandler()).addGems(Integer.parseInt(args[0]));
                    sender.sendMessage(ChatColor.GREEN + "Added " + args[0] + "G!");
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + args[0] + " is not an Integer!");
                }
        } else if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                try {
                    main.getPlayerHandler().getPlayer(target, main.getCurrencyHandler()).addGems(Integer.parseInt(args[1]));
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
