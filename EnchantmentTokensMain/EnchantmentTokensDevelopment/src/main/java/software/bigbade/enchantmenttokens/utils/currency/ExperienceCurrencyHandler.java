/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class ExperienceCurrencyHandler extends EnchantCurrencyHandler {
    private final Player player;

    public ExperienceCurrencyHandler(Player player) {
        super(player, "experience");
        this.player = player;
    }

    @Override
    public CompletableFuture<Long> getAmount() {
        return CompletableFuture.completedFuture((long) player.getLevel());
    }

    @Override
    public void setAmount(long amount) {
        player.setLevel((int) amount);
    }

    @Override
    public void addAmount(long amount) {
        player.giveExpLevels((int) amount);
    }
}
