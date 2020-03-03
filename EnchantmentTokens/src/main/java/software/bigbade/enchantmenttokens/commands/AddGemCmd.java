package software.bigbade.enchantmenttokens.commands;

import software.bigbade.enchantmenttokens.localization.TranslatedMessage;
import software.bigbade.enchantmenttokens.utils.currency.CurrencyAdditionHandler;
import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Formatter;

public class AddGemCmd implements CommandExecutor {
    private EnchantmentPlayerHandler handler;

    private static final Formatter formatter = new Formatter();

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
                addGems(args[0], (Player) sender, sender);
        } else if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                addGems(args[1], target, sender);
            } else {
                sender.sendMessage(TranslatedMessage.translate("command.add.noplayer", args[0]));
            }
        }
        return true;
    }

    private void addGems(String gems, Player target, CommandSender sender) {
        try {
            CurrencyAdditionHandler.addGems(handler.getPlayer(target), Long.parseLong(gems));
        } catch (NumberFormatException e) {
            sender.sendMessage(TranslatedMessage.translate("command.add.notnumber", formatter.format("%,d", gems).toString()));
        }
    }
}
