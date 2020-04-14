/*
 * Addons for the Custom Enchantment API in Minecraft
 * Copyright (C) 2020 BigBadE
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package software.bigbade.enchantmenttokens.localization;

import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.utils.currency.CurrencyAdditionHandler;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class TranslatedPriceMessage implements ITranslatedMessage {
    private static final Map<Locale, ITranslatedMessage> PRICES = getPriceMessages();

    private Locale locale;

    @Override
    public String translate(String... args) {
        if (args.length != 1)
            return "INCORRECT ARGUMENTS";
        return PRICES.get(locale).translate(args);
    }
    public TranslatedPriceMessage(Locale locale) {
        this.locale = locale;
    }

    private static Map<Locale, ITranslatedMessage> getPriceMessages() {
        if (CurrencyAdditionHandler.isUsingGems())
            return loadLocales(StringUtils.GEMS_SYMBOL);
        else if (CurrencyAdditionHandler.isUsingExperience())
            return loadLocales(StringUtils.LEVELS);
        else
            return loadLocales(StringUtils.DOLLAR_SYMBOL);
    }

    private static Map<Locale, ITranslatedMessage> loadLocales(String message) {
        Map<Locale, ITranslatedMessage> locales = new HashMap<>();
        for(Locale locale : LocaleManager.getSupportedLocales()) {
            locales.put(locale, new TranslatedStringMessage(locale, message));
        }
        return locales;
    }
}
