/*
 * Custom enchantments for Minecraft
 * Copyright (C) 2021 Big_Bad_E
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

package com.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import com.bigbade.enchantmenttokens.EnchantmentTokens;

import java.util.Locale;

public class LatestCurrencyHandler extends EnchantCurrencyHandler {
    private final NamespacedKey gems;
    private final NamespacedKey locale;

    public LatestCurrencyHandler(Player player, NamespacedKey gems, NamespacedKey locale) {
        super(player, "gemsNew");
        this.gems = gems;
        this.locale = locale;
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        setAmount(dataContainer.getOrDefault(gems, PersistentDataType.LONG, 0L));
        Locale defaultLocale = Locale.forLanguageTag(player.getLocale());
        if (defaultLocale.getLanguage().isEmpty()) {
            //Some resource packs can mess this up
            defaultLocale = EnchantmentTokens.getDefaultLocale();
        }

        setLocale(Locale.forLanguageTag(dataContainer.getOrDefault(locale, PersistentDataType.STRING, defaultLocale.toLanguageTag())));
    }

    @Override
    public void savePlayer(Player player) {
        player.getPersistentDataContainer().set(gems, PersistentDataType.LONG, getGems());
        player.getPersistentDataContainer().set(locale, PersistentDataType.STRING, getLocale().toLanguageTag());
    }
}
