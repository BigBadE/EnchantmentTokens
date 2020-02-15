package software.bigbade.enchantmenttokens.commands;

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

import software.bigbade.enchantmenttokens.localization.TranslatedMessage;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class EnchantmentListCommand implements CommandExecutor {
    private EnchantmentHandler handler;

    public EnchantmentListCommand(EnchantmentHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("enchanttoken.list") && !sender.isOp()) {
            sender.sendMessage(TranslatedMessage.translate("command.permission"));
            return true;
        }
        StringBuilder builder = new StringBuilder(TranslatedMessage.translate("command.list"));
        handler.getAllEnchants().forEach((enchant) -> builder.append(enchant.getName()).append(", "));
        if (builder.length() > 0)
            builder.setLength(builder.length() - 2);
        sender.sendMessage(builder.toString());
        return true;
    }
}
