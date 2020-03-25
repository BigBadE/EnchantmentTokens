package software.bigbade.enchantmenttokens.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.localization.TranslatedPrice;
import software.bigbade.enchantmenttokens.utils.players.PlayerHandler;

public class BalanceCmd implements CommandExecutor {
    private PlayerHandler handler;

    public BalanceCmd(PlayerHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player) {
            String priceString = new TranslatedPrice().translate("%,d");
            EnchantmentPlayer player = handler.getPlayer((Player) commandSender);
            commandSender.sendMessage(StringUtils.COMMAND_BALANCE.translate(String.format(priceString, player.getGems())));
        }
        return true;
    }
}
