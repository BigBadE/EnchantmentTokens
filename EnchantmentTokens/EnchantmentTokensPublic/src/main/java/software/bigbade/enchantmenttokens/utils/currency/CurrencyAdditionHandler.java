package software.bigbade.enchantmenttokens.utils.currency;

import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.localization.LocaleManager;
import software.bigbade.enchantmenttokens.localization.TranslatedPrice;

import javax.annotation.Nonnull;

public final class CurrencyAdditionHandler {
    private static Boolean usingGems;

    //Private constructor to hide implicit public one
    private CurrencyAdditionHandler() {}

    public static void initialize(boolean usingGems) {
        if (CurrencyAdditionHandler.usingGems != null)
            throw new IllegalStateException("Already initialized");
        CurrencyAdditionHandler.usingGems = usingGems;
    }

    public static String formatMoney(long amount) {
        String priceStr = new TranslatedPrice().translate("%,d");
        return String.format(LocaleManager.getLocale(), priceStr, amount);
    }

    public static void addGems(EnchantmentPlayer player, long amount) {
        player.addGems(amount);
        player.getPlayer().sendMessage(StringUtils.COMMAND_ADD.translate(formatMoney(amount)));
    }

    @Nonnull
    public static Boolean isUsingGems() {
        if (usingGems == null)
            throw new IllegalStateException("Not initialized");
        return usingGems;
    }
}
