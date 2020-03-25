package software.bigbade.enchantmenttokens.commands;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.StringUtils;
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
            return Collections.singletonList(ChatColor.stripColor(StringUtils.COMMAND_ERROR_PERMISSION));
        if (args.length == 1) {
            return getKeys(args[0]);
        } else if (args.length == 2) {
            return checkInt(args[1]);
        } else {
            return Collections.singletonList(ChatColor.stripColor(StringUtils.COMMAND_ERROR_TOO_MANY_ARGUMENTS));
        }
    }

    private List<String> checkInt(String number) {
        try {
            if (!number.isEmpty())
                Integer.parseInt(number);
            return Collections.emptyList();
        } catch (NumberFormatException e) {
            return Collections.singletonList(ChatColor.stripColor(StringUtils.COMMAND_ERROR_NOT_NUMBER.translate(number)));
        }
    }

    public List<String> getKeys(String start) {
        List<String> suggestions = new ArrayList<>();
        for (EnchantmentBase base : handler.getAllEnchants()) {
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
