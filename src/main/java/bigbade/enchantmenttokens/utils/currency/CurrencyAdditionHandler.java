package bigbade.enchantmenttokens.utils.currency;

import bigbade.enchantmenttokens.api.EnchantmentPlayer;
import bigbade.enchantmenttokens.localization.TranslatedMessage;

public class CurrencyAdditionHandler {
    public static void addGems(EnchantmentPlayer player, long amount) {
        player.addGems(amount);

        String priceStr;
        if (player.usingGems())
            priceStr = amount + "G";
        else
            priceStr = TranslatedMessage.translate("dollar.symbol", "" + amount);
        player.getPlayer().sendMessage(TranslatedMessage.translate("command.add", priceStr));
    }
}
