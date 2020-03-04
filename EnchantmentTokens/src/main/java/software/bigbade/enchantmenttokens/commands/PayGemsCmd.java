package software.bigbade.enchantmenttokens.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.localization.TranslatedMessage;
import software.bigbade.enchantmenttokens.utils.currency.CurrencyAdditionHandler;
import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;

import java.util.Formatter;

public class PayGemsCmd implements CommandExecutor {
    private EnchantmentPlayerHandler handler;

    public PayGemsCmd(EnchantmentPlayerHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length != 2) {
            commandSender.sendMessage(TranslatedMessage.translate("command.pay.usage"));
        }
        if(commandSender instanceof Player) {
            Player target = Bukkit.getPlayer(args[0]);
            if(target != null) {
                addGems(args[1], target, commandSender);
            }
        }
        return true;
    }

    private void addGems(String gems, Player target, CommandSender sender) {
        try {
            long gemsLong = Long.parseLong(gems);
            CurrencyAdditionHandler.addGems(handler.getPlayer(target), gemsLong);

            EnchantmentPlayer player = handler.getPlayer((Player) sender);
            player.addGems(-gemsLong);
        } catch (NumberFormatException e) {
            sender.sendMessage(TranslatedMessage.translate("command.add.notnumber", gems));
        }
    }
}