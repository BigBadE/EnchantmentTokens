package software.bigbade.enchantmenttokens.commands;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.VanillaEnchant;
import software.bigbade.enchantmenttokens.localization.TranslatedMessage;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnchantTabCompleter implements TabCompleter, IEnchantTabCompleter {
    private EnchantmentHandler handler;

    public EnchantTabCompleter(EnchantmentHandler handler) {
        this.handler = handler;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("enchanttoken.admin") && !sender.isOp())
            return Collections.singletonList(TranslatedMessage.translate("command.permission"));
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            for (EnchantmentBase base : handler.getAllEnchants()) {
                if (base instanceof VanillaEnchant)
                    continue;
                NamespacedKey trying = base.getKey();
                if (trying.toString().startsWith(args[0]))
                    suggestions.add(trying.toString());
            }
            return suggestions;
        } else if (args.length == 2) {
            try {
                if (!args[1].isEmpty())
                    Integer.parseInt(args[1]);
                return Collections.emptyList();
            } catch (NumberFormatException e) {
                return Collections.singletonList(TranslatedMessage.translate("command.add.notnumber", args[1]));
            }
        } else {
            return Collections.singletonList(TranslatedMessage.translate("command.arguments.toomany"));
        }
    }

    @Override
    public String getPermission() {
        return "enchanttoken.admin";
    }
}
