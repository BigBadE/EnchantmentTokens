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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantMenuCmd implements CommandExecutor {
    public Map<Player, Inventory> inventories = new HashMap<>();

    private ItemStack glassPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {
            Inventory inv = genInventory((Player) commandSender);
            if (inv == null)
                commandSender.sendMessage(ChatColor.RED + "You must hold an item to enchant!");
            else
                ((Player) commandSender).openInventory(inv);
        }
        return true;
    }

    private Inventory genInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, "Enchantments");
        ItemStack item = player.getInventory().getItemInMainHand().clone();
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            if (item.getType() == Material.AIR)
                return null;
            else
                meta = Bukkit.getItemFactory().getItemMeta(item.getType());
        List<String> lore = meta.getLore();
        if (lore == null)
            lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Price: 0G");
        meta.setLore(lore);
        item.setItemMeta(meta);
        inventory.setItem(4, item);
        inventory.setItem(11, makeItem(Material.DIAMOND_PICKAXE, ChatColor.GREEN + "Pickaxe Enchants"));
        inventory.setItem(12, makeItem(Material.DIAMOND_SWORD, ChatColor.GREEN + "Sword Enchants"));
        inventory.setItem(14, makeItem(Material.DIAMOND_CHESTPLATE, ChatColor.GREEN + "Armor Enchants"));
        inventory.setItem(15, makeItem(Material.BOW, ChatColor.GREEN + "Bow Enchants"));
        inventory.setItem(21, makeItem(Material.EMERALD_BLOCK, ChatColor.GREEN + "Enchant"));
        inventory.setItem(23, makeItem(Material.REDSTONE_BLOCK, ChatColor.RED + "Cancel"));
        int i = inventory.firstEmpty();
        while (i > -1 && i < 28) {
            inventory.setItem(i, glassPane);
            i = inventory.firstEmpty();
        }
        inventories.put(player, inventory);
        return inventory;
    }

    private ItemStack makeItem(Material material, String name) {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        stack.setItemMeta(meta);
        return stack;
    }
}
