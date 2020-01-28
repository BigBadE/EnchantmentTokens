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
import bigbade.enchantmenttokens.api.EnchantUtils;
import bigbade.enchantmenttokens.api.EnchantmentBase;
import bigbade.enchantmenttokens.listeners.gui.EnchantmentGUIListener;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class EnchantCmd implements CommandExecutor {
    private EnchantmentTokens main;

    public EnchantCmd(EnchantmentTokens main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("enchanttoken.admin") && !sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to run this command");
            return true;
        }
        if (args.length >= 1 && sender instanceof Player) {
            StringBuilder nameBuilder = new StringBuilder(args[0]);
            for (int i = 1; i < args.length - 1; i++) {
                nameBuilder.append(args[i]);
            }
            String name = nameBuilder.toString();
            ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
            EnchantUtils.addEnchantment(item, name, main, (Player) sender, true);
            for (EnchantmentBase enchant : main.getVanillaEnchantments()) {
                if (enchant.getName().equals(args[0])) {
                    item.addEnchantment(enchant, Integer.parseInt(args[args.length - 1]));
                    ItemMeta meta = item.getItemMeta();
                    if(meta.getLore() == null)
                        meta.setLore(new ArrayList<>());
                    meta.getLore().add(enchant.getName() + ": " + EnchantmentGUIListener.getRomanNumeral(Integer.parseInt(args[args.length - 1])));
                    sender.sendMessage(ChatColor.GREEN + "Added Enchant " + name);
                    return true;
                }
            }
            for (Enchantment enchantment : main.getVanillaEnchantments()) {
                if (enchantment.getKey().getKey().equals(name.toLowerCase().replace("_", ""))) {
                    item.addEnchantment(enchantment, Integer.parseInt(args[args.length - 1]));
                    ItemMeta meta = item.getItemMeta();
                    if(meta.getLore() == null)
                        meta.setLore(new ArrayList<>());
                    meta.getLore().add(enchantment.getName() + ": " + EnchantmentGUIListener.getRomanNumeral(Integer.parseInt(args[args.length - 1])));
                    sender.sendMessage(ChatColor.GREEN + "Added Enchant " + name);
                    return true;
                }
            }
            sender.sendMessage(ChatColor.RED + "Could not find that enchant!");
        } else
            sender.sendMessage("Usage: /adminenchant (enchant) (level)");
        return true;
    }
}
