package software.bigbade.enchantmenttokens.utils.currency;

import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.localization.LocaleManager;
import software.bigbade.enchantmenttokens.localization.TranslatedMoneyMessage;
import software.bigbade.enchantmenttokens.localization.TranslatedTextMessage;

public class CurrencyAdditionHandler {
    private static final TranslatedTextMessage ADDMONEY = new TranslatedTextMessage("command.add");

    private static CurrencyAdditionHandler instance;

    private final boolean usingGems;

    public CurrencyAdditionHandler(boolean usingGems) {
        CurrencyAdditionHandler.setInstance(this);
        this.usingGems = usingGems;
    }

    private static synchronized void setInstance(CurrencyAdditionHandler handler) {
        instance = handler;
    }

    public static synchronized CurrencyAdditionHandler getInstance() {
        return instance;
    }

    public String formatMoney(long amount) {
        String priceStr = new TranslatedMoneyMessage().getStringAmount("%,d");
        return String.format(LocaleManager.getInstance().getLocale(), priceStr, amount);
    }

    public String getFormat() {
        return ADDMONEY.getText("");
    }

    public void addGems(EnchantmentPlayer player, long amount) {
        player.addGems(amount);
        player.getPlayer().sendMessage(ADDMONEY.getText(formatMoney(amount)));
    }

    public boolean isUsingGems() {
        return usingGems;
    }
}
