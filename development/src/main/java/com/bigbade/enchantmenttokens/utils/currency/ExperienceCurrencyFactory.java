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

import org.bukkit.entity.Player;
import com.bigbade.enchantmenttokens.currency.CurrencyFactory;
import com.bigbade.enchantmenttokens.currency.CurrencyHandler;

public class ExperienceCurrencyFactory implements CurrencyFactory {
    @Override
    public CurrencyHandler newInstance(Player player) {
        return new ExperienceCurrencyHandler(player);
    }

    @Override
    public String name() {
        return "experience";
    }

    @Override
    public void shutdown() {
        //Not used since Bukkit handles it
    }

    @Override
    public boolean loaded() {
        return true;
    }
}
