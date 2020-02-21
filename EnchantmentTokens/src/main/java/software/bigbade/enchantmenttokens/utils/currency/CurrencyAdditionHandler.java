package software.bigbade.enchantmenttokens.utils.currency;

import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.localization.TranslatedMessage;

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
