package software.bigbade.enchantmenttokens.commands;

import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.localization.TranslatedMessage;
import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCmd implements CommandExecutor {
    private EnchantmentPlayerHandler handler;

    public BalanceCmd(EnchantmentPlayerHandler handler) {
        this.handler = handler;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            String priceString;
            EnchantmentPlayer player = handler.getPlayer((Player) commandSender);
            if(player.usingGems())
                priceString = player.getGems() + "G";
            else
                priceString = TranslatedMessage.translate("dollar.symbol", "" + player.getGems());
            commandSender.sendMessage(TranslatedMessage.translate("command.balance", priceString));
        }
        return true;
    }
}
