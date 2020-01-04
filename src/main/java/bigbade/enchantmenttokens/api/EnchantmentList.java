package bigbade.enchantmenttokens.api;

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
