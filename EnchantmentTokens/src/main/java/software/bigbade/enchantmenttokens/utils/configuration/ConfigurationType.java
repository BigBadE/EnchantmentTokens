package software.bigbade.enchantmenttokens.utils.configuration;

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

/**
 * Used for safe casting of unknown configuration types
 */
public class ConfigurationType<T> {
    private Object defaultValue;

    public ConfigurationType(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public T getValue(String value, ConfigurationSection section) {
        try {
            return (T) section.get(value);
        } catch (ClassCastException e) {
            return (T) defaultValue;
        }
    }
}
