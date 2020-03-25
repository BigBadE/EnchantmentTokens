package software.bigbade.enchantmenttokens.utils.currency;

import org.jetbrains.annotations.NotNull;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.localization.LocaleManager;
import software.bigbade.enchantmenttokens.localization.TranslatedPrice;

public final class CurrencyAdditionHandler {
    private static Boolean usingGems;

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
        player.getPlayer().sendMessage(StringUtils.ENCHANTMENT_ADD.translate(formatMoney(amount)));
    }

    @NotNull
    public static Boolean isUsingGems() {
        if (usingGems == null)
            throw new IllegalStateException("Not initialized");
        return usingGems;
    }
}
