package bigbade.enchantmenttokens.utils.currency;

import bigbade.enchantmenttokens.EnchantmentTokens;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

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

public class LatestCurrencyHandler implements CurrencyHandler {
    private long gems;

    private static NamespacedKey key;

    public LatestCurrencyHandler(EnchantmentTokens main) {
        key = new NamespacedKey(main, "gems");
    }

    public LatestCurrencyHandler(Player player) {
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        gems = dataContainer.getOrDefault(key, PersistentDataType.LONG, 0L);
    }

    @Override
    public long getAmount() {
        return gems;
    }

    @Override
    public void setAmount(long amount) {
        gems = amount;
    }

    @Override
    public void addAmount(long amount) {
        gems += amount;
    }

    @Override
    public CurrencyHandler newInstance(Player player) {
        return new LatestCurrencyHandler(player);
    }

    @Override
    public void savePlayer(Player player) {
        player.getPersistentDataContainer().set(key, PersistentDataType.LONG, gems);
    }
}