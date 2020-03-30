package software.bigbade.enchantmenttokens.localization;

import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.utils.currency.CurrencyAdditionHandler;

public final class TranslatedPriceMessage implements ITranslatedMessage {
    private static final TranslatedStringMessage PRICE = getPriceMessage();

    @Override
    public String translate(String... args) {
        if (args.length != 1)
            return "INCORRECT ARGUMENTS";
        return PRICE.translate(args);
    }

    private static TranslatedStringMessage getPriceMessage() {
        if (CurrencyAdditionHandler.isUsingGems())
            return StringUtils.GEMS_SYMBOL;
        else if (CurrencyAdditionHandler.isUsingExperience())
            return StringUtils.LEVELS;
        else
            return StringUtils.DOLLAR_SYMBOL;
    }
}
