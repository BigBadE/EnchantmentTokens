package software.bigbade.enchantmenttokens.api;

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
import java.util.function.Consumer;

public enum PriceIncreaseTypes {
    CUSTOM((level, section) -> {
        return section.getInt(level + "");
    }, (enchant) -> {
        for (int i = enchant.minLevel; i < enchant.maxLevel + 1; i++) {
            if (enchant.price.get(i + "") == null) {
                enchant.price.set(i + "", i*10);
            }
        }
        for (String key : enchant.price.getKeys(true)) {
            try {
                if (!key.equals("type"))
                    if (Integer.parseInt(key) < enchant.minLevel || Integer.parseInt(key) > enchant.maxLevel + 1) {
                        enchant.price.set(key, null);
                    }
            } catch (NumberFormatException e) {
                enchant.price.set(key, null);
            }
        }
    });

    private BiFunction<Integer, ConfigurationSection, Integer> function;
    private Consumer<EnchantmentBase> setup;

    PriceIncreaseTypes(BiFunction<Integer, ConfigurationSection, Integer> function, Consumer<EnchantmentBase> setup) {
        this.function = function;
        this.setup = setup;
    }

    public long getPrice(int level, ConfigurationSection section) {
        return function.apply(level, section);
    }

    public void loadConfig(EnchantmentBase base) {
        setup.accept(base);
    }
}
