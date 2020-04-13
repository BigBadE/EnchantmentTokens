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

package software.bigbade.enchantmenttokens.utils.currency;

import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.localization.TranslatedPriceMessage;
import software.bigbade.enchantmenttokens.localization.TranslatedStringMessage;

import java.util.Locale;

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

    public static String formatMoney(Locale locale, long amount) {
        String priceStr = new TranslatedPriceMessage(locale).translate("%,d");
        return String.format(Locale.getDefault(), priceStr, amount);
    }

    public static void addGems(EnchantmentPlayer player, long amount) {
        player.addGems(amount);
        player.getPlayer().sendMessage(new TranslatedStringMessage(player.getLanguage(), StringUtils.COMMAND_ADD).translate(formatMoney(player.getLanguage(), amount)));
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
