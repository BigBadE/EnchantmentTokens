package software.bigbade.enchantmenttokens.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.localization.TranslatedTextMessage;
import software.bigbade.enchantmenttokens.utils.currency.CurrencyAdditionHandler;
import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;

public class BalanceCmd implements CommandExecutor {
    private EnchantmentPlayerHandler handler;

    private static final TranslatedTextMessage BALANCE = new TranslatedTextMessage("command.balance");

    public BalanceCmd(EnchantmentPlayerHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player) {
            EnchantmentPlayer player = handler.getPlayer((Player) commandSender);
            String priceString = CurrencyAdditionHandler.getInstance().formatMoney("" + player.getGems());
            commandSender.sendMessage(BALANCE.getText(priceString));
        }
        return true;
    }
}
