package software.bigbade.enchantmenttokens.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.localization.TranslatedTextMessage;
import software.bigbade.enchantmenttokens.utils.currency.CurrencyAdditionHandler;
import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;

public class PayGemsCmd implements CommandExecutor {
    private EnchantmentPlayerHandler handler;

    public PayGemsCmd(EnchantmentPlayerHandler handler) {
        this.handler = handler;
    }

    private static final String USAGE = new TranslatedTextMessage("command.pay.usage").getText();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length != 2) {
            commandSender.sendMessage(USAGE);
        }
        if(commandSender instanceof Player) {
            Player target = Bukkit.getPlayer(args[0]);
            if(target != null) {
                addGems(args[1], target, commandSender);
            }
        }
        return true;
    }

    private static final TranslatedTextMessage NOTENOUGH = new TranslatedTextMessage("command.pay.notenough");
    private static final TranslatedTextMessage PAY = new TranslatedTextMessage("command.pay");
    private static final TranslatedTextMessage RECIEVE = new TranslatedTextMessage("command.pay.recieve");

    private void addGems(String gems, Player target, CommandSender sender) {
        try {
            long gemsLong = Long.parseLong(gems);
            EnchantmentPlayer player = handler.getPlayer((Player) sender);
            if(gemsLong == 1) {
                sender.sendMessage(NOTENOUGH.getText(CurrencyAdditionHandler.formatMoney(player.usingGems(), 1)));
                return;
            }
            sender.sendMessage(PAY.getText(CurrencyAdditionHandler.formatMoney(player.usingGems(), gemsLong), target.getName()));
            target.sendMessage(RECIEVE.getText(CurrencyAdditionHandler.formatMoney(player.usingGems(), gemsLong), sender.getName()));
            CurrencyAdditionHandler.addGems(handler.getPlayer(target), gemsLong);

            player.addGems(-gemsLong);
        } catch (NumberFormatException e) {
            sender.sendMessage(CommandUtils.NOTANUMBER.getText(gems));
        }
    }
}
