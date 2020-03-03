package software.bigbade.enchantmenttokens.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.localization.TranslatedMessage;
import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;

public class BalanceCmd implements CommandExecutor {
    private EnchantmentPlayerHandler handler;

    public BalanceCmd(EnchantmentPlayerHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player) {
            String priceString;
            EnchantmentPlayer player = handler.getPlayer((Player) commandSender);
            if(player.usingGems())
                priceString = "%,dG";
            else
                priceString = TranslatedMessage.translate("dollar.symbol", "%,d");
            commandSender.sendMessage(TranslatedMessage.translate("command.balance", String.format(priceString, player.getGems())));
        }
        return true;
    }
}
