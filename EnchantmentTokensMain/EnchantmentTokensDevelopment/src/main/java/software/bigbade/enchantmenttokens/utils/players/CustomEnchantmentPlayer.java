/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.utils.players;

import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.currency.CurrencyHandler;
import software.bigbade.enchantmenttokens.gui.EnchantmentGUI;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class CustomEnchantmentPlayer implements EnchantmentPlayer {
    private final Player player;
    private CurrencyHandler handler;

    private EnchantmentGUI currentGUI;

    private long doubler = 1;

    public CustomEnchantmentPlayer(Player player) {
        this.player = player;
    }

    public void setCurrencyHandler(CurrencyHandler hander) {
        this.handler = hander;
    }

    public void save() {
        handler.savePlayer(player);
    }

    public Player getPlayer() {
        return player;
    }

    public CompletableFuture<Long> getGems() {
        return handler.getAmount();
    }

    public void addGems(long amount) {
        handler.addAmount(amount);
    }

    public EnchantmentGUI getCurrentGUI() {
        return currentGUI;
    }

    public long getDoubler() {
        return doubler;
    }

    public void removeDoubler(long removing) {
        doubler -= removing;
    }

    public void resetDoubler() {
        doubler = 1;
    }

    public void addDoubler(long adding) {
        doubler += adding;
    }

    public void setCurrentGUI(EnchantmentGUI currentGUI) {
        this.currentGUI = currentGUI;
    }

    @Override
    public Locale getLanguage() {
        return handler.getLocale();
    }

    @Override
    public void setLanguage(Locale language) {
        handler.setLocale(language);
    }
}
