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
import com.bigbade.enchantmenttokens.loader.FileLoader;
import com.bigbade.enchantmenttokens.loader.GemCurrencyHandler;
import com.bigbade.enchantmenttokens.utils.SchedulerHandler;

public class GemCurrencyFactory extends EnchantCurrencyFactory {
    private final SchedulerHandler scheduler;
    private final FileLoader loader;

    public GemCurrencyFactory(SchedulerHandler scheduler, String dataPath) {
        super("gems");
        this.scheduler = scheduler;
        loader = new FileLoader(dataPath);
    }

    public EnchantCurrencyHandler newInstance(Player player) {
        return new GemCurrencyHandler(player, loader, scheduler);
    }

    @Override
    public boolean loaded() {
        return true;
    }
}
