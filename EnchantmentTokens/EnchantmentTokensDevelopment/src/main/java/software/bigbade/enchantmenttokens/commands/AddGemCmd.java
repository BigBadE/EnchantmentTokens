package software.bigbade.enchantmenttokens.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.utils.currency.CurrencyAdditionHandler;
import software.bigbade.enchantmenttokens.utils.players.PlayerHandler;

import javax.annotation.Nonnull;

public class AddGemCmd implements CommandExecutor {
    private PlayerHandler handler;

    public AddGemCmd(PlayerHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (!sender.hasPermission("enchanttoken.admin") && !sender.isOp()) {
            sender.sendMessage(StringUtils.COMMAND_ERROR_PERMISSION);
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
                sender.sendMessage(StringUtils.COMMAND_ERROR_NO_PLAYER.translate(args[0]));
            }
        }
        return true;
    }

    private void addGems(String gems, Player target, CommandSender sender) {
        try {
            CurrencyAdditionHandler.addGems(handler.getPlayer(target), Long.parseLong(gems));
        } catch (NumberFormatException e) {
            sender.sendMessage(StringUtils.COMMAND_ERROR_NOT_NUMBER.translate(gems));
        }
    }
}
