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
import org.bukkit.persistence.PersistentDataType;
import com.bigbade.enchantmenttokens.EnchantmentTokens;
import com.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import com.bigbade.enchantmenttokens.currency.CurrencyHandler;
import com.bigbade.enchantmenttokens.localization.LocaleManager;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public abstract class EnchantCurrencyHandler implements CurrencyHandler {
    private final Player player;
    private final String name;
    private long gems = -1;
    private Locale locale;

    public EnchantCurrencyHandler(Player player, String name) {
        this.locale = Locale.forLanguageTag(player.getLocale());
        if (locale.toLanguageTag().isEmpty()) {
            //Some resource packs can mess this up
            this.locale = EnchantmentTokens.getDefaultLocale();
        }
        this.name = name;
        this.player = player;
    }

    public long getGems() {
        return gems;
    }

    public void setAmount(long amount) {
        gems = amount;
    }

    public void addAmount(long amount) {
        gems += amount;
    }

    public void savePlayer(Player player) {
    }

    @Override
    public void savePlayer(EnchantmentPlayer enchantmentPlayer) {
    }

    @Override
    public void storePlayerData(NamespacedKey key, String value) {
        player.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
    }

    @Override
    public String getPlayerData(NamespacedKey key) {
        return player.getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    @Override
    public void removePlayerData(NamespacedKey key) {
        player.getPersistentDataContainer().remove(key);
    }

    @Override
    public CompletableFuture<Long> getAmount() {
        return CompletableFuture.completedFuture(gems);
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public void setLocale(Locale language) {
        locale = language;
    }

    public String name() {
        return name;
    }
}
