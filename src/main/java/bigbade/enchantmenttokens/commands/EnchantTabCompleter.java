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

import bigbade.enchantmenttokens.api.EnchantmentBase;
import bigbade.enchantmenttokens.utils.EnchantmentHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.List;

public class EnchantTabCompleter implements TabCompleter {
    private EnchantmentHandler handler;

    public EnchantTabCompleter(EnchantmentHandler handler) {
        this.handler = handler;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length >= 1) {
            StringBuilder nameBuilder = new StringBuilder(args[0]);
            for (int i = 1; i < args.length - 1; i++) {
                nameBuilder.append(args[i]);
            }
            String name = nameBuilder.toString();
            List<String> suggestions = new ArrayList<>();
            for (EnchantmentBase base : handler.getEnchantments()) {
                String edited = base.getName();
                if (edited.contains(name))
                    suggestions.add(edited);
            }
            for (Enchantment base : handler.getVanillaEnchants()) {
                String edited = base.getKey().getKey().toLowerCase().replace("_", " ");
                if (edited.contains(name))
                    suggestions.add(edited);
            }
            return suggestions;
        }
        return new ArrayList<>();
    }
}
