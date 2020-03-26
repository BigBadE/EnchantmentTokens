package software.bigbade.enchantmenttokens.localization;

import software.bigbade.enchantmenttokens.utils.currency.CurrencyAdditionHandler;

public class TranslatedMoneyMessage implements ITranslatedText {
    private static final TranslatedTextMessage money = new TranslatedTextMessage("dollar.symbol");
    private static final TranslatedTextMessage gems = new TranslatedTextMessage("gems.symbol");

    public String getStringAmount(String amount) {
        if (CurrencyAdditionHandler.getInstance().isUsingGems())
            return gems.getText(amount);
        else
            return money.getText(amount);
    }

    private static final String ERROR = "Use TranslatedMoney#getStringAmount(long), not TranslatedMoney#getText()";

    @Override
    public String getText() {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public String getText(String... args) {
        throw new IllegalArgumentException(ERROR);
    }
}
