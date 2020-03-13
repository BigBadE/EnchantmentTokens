package software.bigbade.enchantmenttokens.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import software.bigbade.enchantmenttokens.utils.currency.CurrencyAdditionHandler;
import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;

public class AddGemCmd implements CommandExecutor {
    private EnchantmentPlayerHandler handler;

    public AddGemCmd(EnchantmentPlayerHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("enchanttoken.admin") && !sender.isOp()) {
            sender.sendMessage(CommandUtils.NOPERMISSION);
            return true;
        }
        if (args.length == 1) {
            if (sender instanceof Player)
                addGems(args[0], (Player) sender, sender);
        } else if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                addGems(args[1], target, sender);
            } else {
                sender.sendMessage(CommandUtils.NOPLAYER.getText(args[0]));
            }
        }
        return true;
    }

    private void addGems(String gems, Player target, CommandSender sender) {
        try {
            CurrencyAdditionHandler.addGems(handler.getPlayer(target), Long.parseLong(gems));
        } catch (NumberFormatException e) {
            sender.sendMessage(CommandUtils.NOTANUMBER.getText(gems));
        }
    }
}
