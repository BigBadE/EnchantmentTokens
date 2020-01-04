package bigbade.enchantmenttokens.commands;

import bigbade.enchantmenttokens.EnchantmentTokens;
import bigbade.enchantmenttokens.api.EnchantmentBase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.List;

public class EnchantTabCompleter implements TabCompleter {
    private EnchantmentTokens main;

    public EnchantTabCompleter(EnchantmentTokens main) {
        this.main = main;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1) {
            StringBuilder nameBuilder = new StringBuilder(args[0]);
            for (int i = 1; i < args.length - 1; i++) {
                nameBuilder.append(args[i]);
            }
            String name = nameBuilder.toString();
            List<String> suggestions = new ArrayList<>();
            for (EnchantmentBase base : main.enchantments) {
                String edited = base.getName();
                if (edited.contains(name))
                    suggestions.add(edited);
            }
            for (Enchantment base : main.vanillaEnchants) {
                String edited = base.getKey().getKey().toLowerCase().replace("_", " ");
                if (edited.contains(name))
                    suggestions.add(edited);
            }
            return suggestions;
        }
        return new ArrayList<>();
    }
}
