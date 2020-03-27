package software.bigbade.enchantmenttokens.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;

import javax.annotation.Nonnull;

public class EnchantmentListCommand implements CommandExecutor {
    private EnchantmentHandler handler;

    public EnchantmentListCommand(EnchantmentHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (!sender.hasPermission("enchanttoken.list") && !sender.isOp()) {
            sender.sendMessage(StringUtils.COMMAND_ERROR_PERMISSION);
        } else {
            StringBuilder builder = new StringBuilder();
            handler.getAllEnchants().forEach(enchant -> builder.append(enchant.getEnchantName()).append(" (").append(enchant.getKey().getKey()).append(")").append(", "));
            if (builder.length() > 0)
                builder.setLength(builder.length() - 2);
            sender.sendMessage(StringUtils.COMMAND_LIST.translate(builder.toString()));
        }
        return true;
    }
}
