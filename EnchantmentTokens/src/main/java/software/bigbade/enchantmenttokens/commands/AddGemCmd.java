package software.bigbade.enchantmenttokens.commands;

import software.bigbade.enchantmenttokens.localization.TranslatedMessage;
import software.bigbade.enchantmenttokens.utils.currency.CurrencyAdditionHandler;
import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddGemCmd implements CommandExecutor {
    private EnchantmentPlayerHandler handler;

    public AddGemCmd(EnchantmentPlayerHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("enchanttoken.admin") && !sender.isOp()) {
            sender.sendMessage(TranslatedMessage.translate("command.permission"));
            return true;
        }
        if (args.length == 1) {
            if (sender instanceof Player)
                try {
                    CurrencyAdditionHandler.addGems(handler.getPlayer((Player) sender), Integer.parseInt(args[0]));
                } catch (NumberFormatException e) {
                    sender.sendMessage(TranslatedMessage.translate("command.add.notint", args[0]));
                }
        } else if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                try {
                    CurrencyAdditionHandler.addGems(handler.getPlayer((Player) sender), Integer.parseInt(args[1]));
                } catch (NumberFormatException e) {
                    sender.sendMessage(TranslatedMessage.translate("command.add.notint", args[1]));
                }
            } else {
                sender.sendMessage(TranslatedMessage.translate("command.add.noplayer", args[0]));
            }
        }
        return true;
    }
}
