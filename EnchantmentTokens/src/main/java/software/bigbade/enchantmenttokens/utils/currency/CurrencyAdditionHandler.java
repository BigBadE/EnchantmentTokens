package software.bigbade.enchantmenttokens.utils.currency;

import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.localization.TranslatedMessage;

import java.util.Formatter;

public class CurrencyAdditionHandler {

    private static final Formatter formatter = new Formatter();

    //Private constructor to hide implicit public one.
    private CurrencyAdditionHandler() {}

    public static void addGems(EnchantmentPlayer player, long amount) {
        player.addGems(amount);

        String priceStr;
        if (player.usingGems())
            priceStr = amount + "%,dG";
        else
            priceStr = TranslatedMessage.translate("dollar.symbol", "%,d");
        player.getPlayer().sendMessage(TranslatedMessage.translate("command.add", formatter.format(priceStr, amount).toString()));
    }
}
