package software.bigbade.enchantmenttokens.utils.currency;

import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.localization.LocaleManager;
import software.bigbade.enchantmenttokens.localization.TranslatedMoneyMessage;
import software.bigbade.enchantmenttokens.localization.TranslatedTextMessage;

public class CurrencyAdditionHandler {
    private static final TranslatedTextMessage ADDMONEY = new TranslatedTextMessage("command.add");

    //Private constructor to hide implicit public one.
    private CurrencyAdditionHandler() {}

    public static String formatMoney(boolean usingGems, long amount) {
        String priceStr = new TranslatedMoneyMessage(usingGems).getStringAmount("%,d");
        return String.format(LocaleManager.locale, priceStr, amount);
    }

    public static void addGems(EnchantmentPlayer player, long amount) {
        player.addGems(amount);
        player.getPlayer().sendMessage(ADDMONEY.getText(formatMoney(player.usingGems(), amount)));
    }
}
