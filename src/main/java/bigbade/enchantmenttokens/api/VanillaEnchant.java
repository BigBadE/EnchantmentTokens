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

import bigbade.enchantmenttokens.EnchantmentTokens;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;

public class VanillaEnchant extends EnchantmentBase {
    public Enchantment enchantment;

    public VanillaEnchant(EnchantmentTokens main, ConfigurationSection config, Material icon, Enchantment enchantment) {
        super(main, enchantment.getName(), config, icon);
        this.enchantment = enchantment;
    }

    public VanillaEnchant loadConfig(ConfigurationSection section) {

        return this;
    }
}