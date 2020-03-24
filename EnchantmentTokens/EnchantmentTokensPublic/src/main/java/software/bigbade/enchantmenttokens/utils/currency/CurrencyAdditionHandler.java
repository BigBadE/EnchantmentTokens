package software.bigbade.enchantmenttokens.utils.currency;

import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.localization.LocaleManager;
import software.bigbade.enchantmenttokens.localization.TranslatedPrice;

public class CurrencyAdditionHandler {
    private static boolean usingGems;

    public CurrencyAdditionHandler(boolean usingGems) {
        setGems(usingGems);
    }

    private static void setGems(boolean gems) {
        usingGems = gems;
    }

    public String formatMoney(long amount) {
        String priceStr = new TranslatedPrice().translate("%,d");
        return String.format(LocaleManager.getLocale(), priceStr, amount);
    }

    public void addGems(EnchantmentPlayer player, long amount) {
        player.addGems(amount);
        player.getPlayer().sendMessage(ADDMONEY.translate(formatMoney(amount)));
    }

    public static boolean isUsingGems() {
        return usingGems;
    }
}
