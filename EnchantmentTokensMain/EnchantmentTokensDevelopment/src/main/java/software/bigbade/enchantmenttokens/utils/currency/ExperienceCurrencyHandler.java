/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.entity.Player;

public class ExperienceCurrencyHandler extends EnchantCurrencyHandler {
    private final Player player;

    public ExperienceCurrencyHandler(Player player) {
        super(player, "experience");
        this.player = player;
    }

    @Override
    public long getAmount() {
        return player.getLevel();
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
