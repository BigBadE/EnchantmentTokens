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
