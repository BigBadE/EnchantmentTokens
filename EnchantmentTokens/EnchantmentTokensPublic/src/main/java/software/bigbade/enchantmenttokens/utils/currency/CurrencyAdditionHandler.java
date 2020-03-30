package software.bigbade.enchantmenttokens.utils.currency;

import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.localization.LocaleManager;
import software.bigbade.enchantmenttokens.localization.TranslatedPriceMessage;

import javax.annotation.Nonnull;

public final class CurrencyAdditionHandler {
    private static Boolean usingGems;
    private static Boolean usingXp;

    //Private constructor to hide implicit public one
    private CurrencyAdditionHandler() {}

    public static void initialize(boolean usingGems, boolean usingXp) {
        if (CurrencyAdditionHandler.usingGems != null || CurrencyAdditionHandler.usingXp != null)
            throw new IllegalStateException("Already initialized");
        CurrencyAdditionHandler.usingGems = usingGems;
        CurrencyAdditionHandler.usingXp = usingXp;
    }

    public static String formatMoney(long amount) {
        String priceStr = new TranslatedPriceMessage().translate("%,d");
        return String.format(LocaleManager.getLocale(), priceStr, amount);
    }

    public static void addGems(EnchantmentPlayer player, long amount) {
        player.addGems(amount);
        player.getPlayer().sendMessage(StringUtils.COMMAND_ADD.translate(formatMoney(amount)));
    }

    public static boolean isUsingGems() {
        if (usingGems == null)
            throw new IllegalStateException("Not initialized");
        return usingGems;
    }

    public static boolean isUsingExperience() {
        if (usingXp == null)
            throw new IllegalStateException("Not initialized");
        return usingXp;
    }
}
