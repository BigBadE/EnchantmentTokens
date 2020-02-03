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

import bigbade.enchantmenttokens.gui.EnchantmentGUI;
import bigbade.enchantmenttokens.gui.EnchantmentMenuFactory;
import bigbade.enchantmenttokens.localization.TranslatedMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnchantMenuCmd implements CommandExecutor {
    private EnchantmentMenuFactory factory;

    public EnchantMenuCmd(EnchantmentMenuFactory factory) {
        this.factory = factory;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {
            EnchantmentGUI inv = factory.genInventory((Player) commandSender);
            if (inv == null)
                commandSender.sendMessage(TranslatedMessage.translate("command.enchant.held"));
        }
        return true;
    }
}
