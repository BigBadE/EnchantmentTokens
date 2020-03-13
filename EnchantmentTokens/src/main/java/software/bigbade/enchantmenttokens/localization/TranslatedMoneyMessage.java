package software.bigbade.enchantmenttokens.localization;

public class TranslatedMoneyMessage implements ITranslatedText {
    private static final TranslatedTextMessage money = new TranslatedTextMessage("dollar.symbol");
    private static final TranslatedTextMessage gems = new TranslatedTextMessage("gems.symbol");

    private boolean usingGems;

    public TranslatedMoneyMessage(boolean usingGems) {
        this.usingGems = usingGems;
    }

    public String getStringAmount(String amount) {
        if (usingGems)
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
