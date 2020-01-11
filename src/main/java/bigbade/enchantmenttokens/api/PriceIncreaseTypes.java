package bigbade.enchantmenttokens.api;

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

import org.bukkit.configuration.ConfigurationSection;

import java.util.function.BiFunction;

public enum PriceIncreaseTypes {
    CUSTOM((level, section) -> {
        ConfigurationSection prices = section.getConfigurationSection("prices");
        if(prices == null)
            prices = section.createSection("prices");
        return prices.getInt(level + "");
    });

    private BiFunction<Integer, ConfigurationSection, Integer> function;
    PriceIncreaseTypes(BiFunction<Integer, ConfigurationSection, Integer> function) {
        this.function = function;
    }

    public long getPrice(int level, ConfigurationSection section) {
        return function.apply(level, section);
    }
}
