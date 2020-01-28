package bigbade.enchantmenttokens.utils.currency;

import bigbade.enchantmenttokens.EnchantmentTokens;
import bigbade.enchantmenttokens.loader.FileLoader;
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

public class GemCurrencyHandler implements CurrencyHandler {
    private long gems;
    private FileLoader fileLoader;

    public GemCurrencyHandler(EnchantmentTokens main) {
        fileLoader = new FileLoader(main);
    }

    public GemCurrencyHandler(Player player, FileLoader fileLoader) {
        this.fileLoader = fileLoader;
        gems = fileLoader.getGems(player);
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
    public void removeAmount(long amount) {
        gems -= amount;
    }

    @Override
    public void addAmount(long amount) {
        gems += amount;
    }

    @Override
    public CurrencyHandler newInstance(Player player) {
        return new GemCurrencyHandler(player, fileLoader);
    }


    @Override
    public void save() {
        fileLoader.saveCache();
    }

    @Override
    public void savePlayer(Player player, EnchantmentTokens main) {
        fileLoader.removePlayer(player);
    }
}
