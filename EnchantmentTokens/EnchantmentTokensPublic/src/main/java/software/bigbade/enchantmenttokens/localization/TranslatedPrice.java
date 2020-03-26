package software.bigbade.enchantmenttokens.localization;

import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.utils.currency.CurrencyAdditionHandler;

public class TranslatedPrice implements ITranslatedMessage {
    private static final TranslatedString PRICE = CurrencyAdditionHandler.isUsingGems() ? StringUtils.GEMS_SYMBOL : StringUtils.DOLLAR_SYMBOL;
    @Override
    public String translate(String... args) {
        if(args.length != 1)
            return "INCORRECT ARGUMENTS";
        return PRICE.translate(args);
    }
}
