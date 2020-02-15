package software.bigbade.enchantmenttokens.utils.currency;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

/*
EnchantmentTokens
Copyright (C) 2019-2020 Big_Bad_E
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

public class VaultCurrencyHandler implements CurrencyHandler {
    private Player player;
    private Economy economy;

    public VaultCurrencyHandler(Player player, Economy economy) {
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
    public void savePlayer(Player player) { }

    @Override
    public String name() {
        return "vault";
    }
}
