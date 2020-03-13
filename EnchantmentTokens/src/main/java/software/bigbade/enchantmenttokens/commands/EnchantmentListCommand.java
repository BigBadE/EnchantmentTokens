package software.bigbade.enchantmenttokens.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import software.bigbade.enchantmenttokens.localization.TranslatedTextMessage;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;

public class EnchantmentListCommand implements CommandExecutor {
    private EnchantmentHandler handler;

    public EnchantmentListCommand(EnchantmentHandler handler) {
        this.handler = handler;
    }

    private static final TranslatedTextMessage LIST = new TranslatedTextMessage("command.list");
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("enchanttoken.list") && !sender.isOp()) {
            sender.sendMessage(CommandUtils.NOPERMISSION);
        } else {
            StringBuilder builder = new StringBuilder();
            handler.getAllEnchants().forEach(enchant -> builder.append(enchant.getName()).append(", "));
            if (builder.length() > 0)
                builder.setLength(builder.length() - 2);
            sender.sendMessage(LIST.getText(builder.toString()));
        }
        return true;
    }
}
