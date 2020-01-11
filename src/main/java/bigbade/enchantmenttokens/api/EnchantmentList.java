package bigbade.enchantmenttokens.api;

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
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class EnchantmentList implements CommandExecutor {
    private EnchantmentTokens main;

    public EnchantmentList(EnchantmentTokens main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        StringBuilder builder = new StringBuilder("Enchantments: ");
        if(main.enchantments.size() > 0) {
            main.enchantments.forEach((enchant) -> builder.append(enchant.getName()).append(", "));
            builder.setLength(builder.length() - 2);
        }
        sender.sendMessage(ChatColor.GREEN + builder.toString());
        return true;
    }
}
