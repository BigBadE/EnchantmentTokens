package bigbade.enchantmenttokens.utils;

import bigbade.enchantmenttokens.EnchantmentTokens;
import bigbade.enchantmenttokens.api.EnchantmentPlayer;
import bigbade.enchantmenttokens.localization.TranslatedMessage;
import org.bukkit.entity.Player;

public class CurrencyHandler {
    public static void addGems(Player player, long amount, EnchantmentTokens main) {
        main.getPlayerHandler().getPlayer((Player) sender, main.getCurrencyHandler());
    }

    public static void addGems(EnchantmentPlayer player, long amount, EnchantmentTokens main) {
        EnchantmentPlayer enchantmentPlayer =
        enchantmentPlayer.addGems(Integer.parseInt(args[0]));

        String priceStr;
        if (enchantmentPlayer.usingGems())
            priceStr = args[0] + "G";
        else
            priceStr = TranslatedMessage.translate("dollar.symbol") + args[0];
        sender.sendMessage(TranslatedMessage.translate("command.add", priceStr));
    }
}
