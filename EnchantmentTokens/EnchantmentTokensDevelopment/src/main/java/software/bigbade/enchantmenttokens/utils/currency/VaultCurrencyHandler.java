/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.utils.currency;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

import java.util.Locale;

public class VaultCurrencyHandler extends EnchantCurrencyHandler {
    private Player player;
    private Economy economy;

    public VaultCurrencyHandler(Player player, Economy economy) {
        super("vault");
        this.player = player;
        this.economy = economy;
    }

    @Override
    public long getAmount() {
        return (long) economy.getBalance(player);
    }

    @Override
    public void setAmount(long amount) {
        economy.withdrawPlayer(player, getAmount());
        economy.depositPlayer(player, amount);
    }

    @Override
    public void addAmount(long amount) {
        economy.depositPlayer(player, amount);
    }

    @Override
    public Locale getLocale() {
        return Locale.getDefault();
    }

    @Override
    public void setLocale(Locale language) {
        //Not supported by Vault
    }
}
