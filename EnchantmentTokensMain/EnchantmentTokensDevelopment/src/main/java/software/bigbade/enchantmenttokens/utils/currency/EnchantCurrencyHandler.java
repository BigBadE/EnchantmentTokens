/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.currency.CurrencyHandler;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public abstract class EnchantCurrencyHandler implements CurrencyHandler {
    private long gems = -1;
    private final String name;
    private Locale locale;

    public EnchantCurrencyHandler(Player player, String name) {
        try {
            this.locale = Locale.forLanguageTag(player.getLocale());
        } catch (NullPointerException e) {
            //Some resource packs can mess this up
            this.locale = Locale.getDefault();
        }
        this.name = name;
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
