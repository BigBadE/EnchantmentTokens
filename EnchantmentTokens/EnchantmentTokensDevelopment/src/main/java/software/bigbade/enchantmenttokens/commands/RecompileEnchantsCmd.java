package software.bigbade.enchantmenttokens.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.StringUtils;

import javax.annotation.Nonnull;

public class RecompileEnchantsCmd implements CommandExecutor {
    private EnchantmentTokens main;

    public RecompileEnchantsCmd(EnchantmentTokens main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if(!sender.hasPermission("enchanttoken.admin") && !sender.isOp()) {
            sender.sendMessage(ChatColor.stripColor(StringUtils.COMMAND_ERROR_PERMISSION));
        } else {
            main.unregisterEnchants();
            main.registerEnchants();
        }
        return true;
    }
}
