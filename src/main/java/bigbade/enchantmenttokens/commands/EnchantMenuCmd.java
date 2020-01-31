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
import bigbade.enchantmenttokens.api.EnchantmentPlayer;
import bigbade.enchantmenttokens.gui.EnchantmentGUI;
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
import java.util.List;

public class EnchantMenuCmd implements CommandExecutor {
    private ItemStack glassPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
    private int version;
    private EnchantmentTokens main;

    public EnchantMenuCmd(int version, EnchantmentTokens main) {
        this.version = version;
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {
            Inventory inv = genInventory((Player) commandSender);
            if (inv == null)
                commandSender.sendMessage(ChatColor.RED + "You must hold an item to enchant!");
        }
        return true;
    }

    public Inventory genInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, "Enchantments");
        ItemStack item = player.getInventory().getItemInMainHand().clone();
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            if (item.getType() == Material.AIR)
                return null;
            else
                meta = Bukkit.getItemFactory().getItemMeta(item.getType());
        assert meta != null;
        List<String> lore = meta.getLore();
        if (lore == null)
            lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Price: 0G");
        meta.setLore(lore);
        item.setItemMeta(meta);
        inventory.setItem(4, item);
        inventory.setItem(11, makeItem(Material.DIAMOND_PICKAXE, ChatColor.GREEN + "Tool Enchants"));
        inventory.setItem(12, makeItem(Material.DIAMOND_SWORD, ChatColor.GREEN + "Sword Enchants"));

        inventory.setItem(14, makeItem(Material.DIAMOND_CHESTPLATE, ChatColor.GREEN + "Armor Enchants"));
        inventory.setItem(15, makeItem(Material.BOW, ChatColor.GREEN + "Bow Enchants"));

        if (version >= 14) {
            inventory.setItem(9, makeItem(Material.CROSSBOW, ChatColor.GREEN + "Crossbow Enchants"));
            inventory.setItem(10, makeItem(Material.TRIDENT, ChatColor.GREEN + "Trident Enchants"));
            inventory.setItem(16, makeItem(Material.FISHING_ROD, ChatColor.GREEN + "Fishing Rod Enchants"));
            inventory.setItem(17, makeItem(Material.SHIELD, ChatColor.GREEN + "Shield Enchants"));
        } else if (version >= 13) {
            inventory.setItem(10, makeItem(Material.TRIDENT, ChatColor.GREEN + "Trident Enchants"));
            inventory.setItem(13, makeItem(Material.FISHING_ROD, ChatColor.GREEN + "Fishing Rod Enchants"));
            inventory.setItem(16, makeItem(Material.SHIELD, ChatColor.GREEN + "Shield Enchants"));
        } else if (version >= 9) {
            inventory.setItem(10, makeItem(Material.FISHING_ROD, ChatColor.GREEN + "Fishing Rod Enchants"));
            inventory.setItem(16, makeItem(Material.SHIELD, ChatColor.GREEN + "Shield Enchants"));
        } else
            inventory.setItem(13, makeItem(Material.FISHING_ROD, ChatColor.GREEN + "Fishing Rod Enchants"));
        inventory.setItem(21, makeItem(Material.EMERALD_BLOCK, ChatColor.GREEN + "Enchant"));
        inventory.setItem(23, makeItem(Material.REDSTONE_BLOCK, ChatColor.RED + "Cancel"));
        int i = inventory.firstEmpty();
        while (i > -1 && i < 28) {
            inventory.setItem(i, glassPane);
            i = inventory.firstEmpty();
        }
        EnchantmentPlayer enchantPlayer = main.getPlayerHandler().getPlayer(player);
        player.openInventory(inventory);
        enchantPlayer.setCurrentGUI(new EnchantmentGUI(inventory));
        return inventory;
    }

    private ItemStack makeItem(Material material, String name) {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        stack.setItemMeta(meta);
        return stack;
    }
}
