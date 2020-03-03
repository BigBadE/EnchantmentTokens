package software.bigbade.enchantmenttokens.commands;

import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.localization.TranslatedMessage;
import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Formatter;

public class BalanceCmd implements CommandExecutor {
    private EnchantmentPlayerHandler handler;

    private static final Formatter formatter = new Formatter();

    public BalanceCmd(EnchantmentPlayerHandler handler) {
        this.handler = handler;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            String priceString;
            EnchantmentPlayer player = handler.getPlayer((Player) commandSender);
            if(player.usingGems())
                priceString = "%,dG";
            else
                priceString = TranslatedMessage.translate("dollar.symbol", "%,d");
            commandSender.sendMessage(TranslatedMessage.translate("command.balance", formatter.format(priceString, player.getGems()).toString()));
        }
        return true;
    }
}
