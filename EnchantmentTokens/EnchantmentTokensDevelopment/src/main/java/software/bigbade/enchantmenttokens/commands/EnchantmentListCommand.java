package software.bigbade.enchantmenttokens.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import software.bigbade.enchantmenttokens.localization.TranslatedMessage;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;

public class EnchantmentListCommand implements CommandExecutor {
    private EnchantmentHandler handler;

    public EnchantmentListCommand(EnchantmentHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("enchanttoken.list") && !sender.isOp()) {
            sender.sendMessage(TranslatedMessage.translate("command.permission"));
        } else {
            StringBuilder builder = new StringBuilder(TranslatedMessage.translate("command.list"));
            handler.getAllEnchants().forEach(enchant -> builder.append(enchant.getName()).append(", "));
            if (builder.length() > 0)
                builder.setLength(builder.length() - 2);
            sender.sendMessage(builder.toString());
        }
        return true;
    }
}