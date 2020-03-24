package software.bigbade.enchantmenttokens.commands;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import software.bigbade.enchantmenttokens.api.CustomEnchantment;
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
            return getKeys(args[0]);
        } else if (args.length == 2) {
            return checkInt(args[1]);
        } else {
            return Collections.singletonList(TranslatedMessage.translate("command.arguments.toomany"));
        }
    }

    private List<String> checkInt(String number) {
        try {
            if (!number.isEmpty())
                Integer.parseInt(number);
            return Collections.emptyList();
        } catch (NumberFormatException e) {
            return Collections.singletonList(TranslatedMessage.translate("command.add.notnumber", number));
        }
    }

    public List<String> getKeys(String start) {
        List<String> suggestions = new ArrayList<>();
        for (CustomEnchantment base : handler.getAllEnchants()) {
            if (base instanceof VanillaEnchant)
                continue;
            NamespacedKey trying = base.getKey();
            if (trying.toString().startsWith(start)) {
                suggestions.add(trying.toString());
                if(suggestions.size() > 5)
                    break;
            }
        }
        return suggestions;
    }

    @Override
    public String getPermission() {
        return "enchanttoken.admin";
    }
}
