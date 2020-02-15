package software.bigbade.enchantmenttokens.utils.currency;

import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.ExternalCurrencyData;
import software.bigbade.enchantmenttokens.loader.FileLoader;
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

public class GemCurrencyFactory implements CurrencyFactory {
    private EnchantmentTokens main;
    private FileLoader loader;
    private ExternalCurrencyData data;

    public GemCurrencyFactory(EnchantmentTokens main) {
        this.main = main;
        loader = new FileLoader(main.getDataFolder().getAbsolutePath());
    }

    public CurrencyHandler newInstance(Player player) {
        return new GemCurrencyHandler(player, loader, main.getScheduler());
    }

    @Override
    public String name() {
        return "gemsOld";
    }

    @Override
    public void shutdown() {
        //Not used
    }
}
