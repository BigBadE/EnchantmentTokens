package bigbade.enchantmenttokens.commands;

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

public class EnchantCmd implements CommandExecutor {
    private EnchantmentTokens main;

    public EnchantCmd(EnchantmentTokens main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 2 && sender instanceof Player) {
            StringBuilder nameBuilder = new StringBuilder(args[1]);
            for (int i = 1; i < args.length - 1; i++) {
                nameBuilder.append(args[i]);
            }
            String name = nameBuilder.toString();
            ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
            EnchantUtils.addEnchantment(item, name, main, (Player) sender, main.getConfig().getConfigurationSection("enchants"), true);
            for (EnchantmentBase enchant : main.enchantments) {
                if (enchant.getName().equals(args[0])) {
                    item.addEnchantment(enchant, Integer.parseInt(args[args.length - 1]));
                    ItemMeta meta = item.getItemMeta();
                    meta.getLore().add(enchant.getName() + ": " + EnchantmentGUIListener.getRomanNumeral(Integer.parseInt(args[args.length - 1])));
                    sender.sendMessage(ChatColor.GREEN + "Added Enchant " + name);
                    return true;
                }
            }
            for (Enchantment enchantment : main.vanillaEnchants) {
                if (enchantment.getKey().getKey().equals(name.toLowerCase().replace("_", ""))) {
                    item.addEnchantment(enchantment, Integer.parseInt(args[args.length - 1]));
                    ItemMeta meta = item.getItemMeta();
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
