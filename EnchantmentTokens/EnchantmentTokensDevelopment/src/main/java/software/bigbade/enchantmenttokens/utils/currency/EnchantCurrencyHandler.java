/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.currency.CurrencyHandler;

import java.util.Locale;

public abstract class EnchantCurrencyHandler implements CurrencyHandler {
    private long gems = -1;
    private String name;
    private Locale locale;

    public EnchantCurrencyHandler(String name) {
        this.name = name;
    }

    public long getAmount() {
        return gems;
    }

    public void setAmount(long amount) {
        gems = amount;
    }

    public void addAmount(long amount) {
        gems += amount;
    }

    public void savePlayer(Player player, boolean async) { }

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
