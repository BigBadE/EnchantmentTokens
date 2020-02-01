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

import bigbade.enchantmenttokens.localization.TranslatedMessage;
import bigbade.enchantmenttokens.utils.CurrencyAdditionHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddGemCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("enchanttoken.admin") && !sender.isOp()) {
            sender.sendMessage(TranslatedMessage.translate("command.permission"));
            return true;
        }
        if (args.length == 1) {
            if (sender instanceof Player)
                try {
                    CurrencyAdditionHandler.addGems((Player) sender, Integer.parseInt(args[0]));
                } catch (NumberFormatException e) {
                    sender.sendMessage(TranslatedMessage.translate("command.add.notint", args[0]));
                }
        } else if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                try {
                    CurrencyAdditionHandler.addGems(target, Integer.parseInt(args[1]));
                } catch (NumberFormatException e) {
                    sender.sendMessage(TranslatedMessage.translate("command.add.notint", args[1]));
                }
            } else {
                sender.sendMessage(TranslatedMessage.translate("command.add.noplayer", args[0]));
            }
        }
        return true;
    }
}
