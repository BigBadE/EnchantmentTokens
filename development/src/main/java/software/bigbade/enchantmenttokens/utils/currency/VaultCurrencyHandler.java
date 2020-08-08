/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.utils.currency;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class VaultCurrencyHandler extends EnchantCurrencyHandler {
    private final Player player;
    private final Economy economy;

    public VaultCurrencyHandler(Player player, Economy economy) {
        super(player, "vault");
        this.player = player;
        this.economy = economy;
    }

    @Override
    public CompletableFuture<Long> getAmount() {
        return CompletableFuture.completedFuture((long) economy.getBalance(player));
    }

    @Override
    public void setAmount(long amount) {
        economy.withdrawPlayer(player, getGems());
        economy.depositPlayer(player, amount);
    }

    @Override
    public void addAmount(long amount) {
        if (amount < 0) {
            economy.withdrawPlayer(player, -amount);
        } else {
            economy.depositPlayer(player, amount);
        }
    }
}
