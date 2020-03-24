package software.bigbade.enchantmenttokens.localization;

import software.bigbade.enchantmenttokens.utils.currency.CurrencyAdditionHandler;

public class TranslatedPrice implements ITranslatedMessage {
    private static final TranslatedString PRICE = CurrencyAdditionHandler.isUsingGems() ? new TranslatedString("gems.symbol") : new TranslatedString("dollar.symbol");
    @Override
    public String translate(String... args) {
        if(args.length != 1)
            return "INCORRECT ARGUEMTNS";
        return PRICE.translate(args);
    }
}
