/*
 * Addons for the Custom Enchantment API in Minecraft
 * Copyright (C) 2020 BigBadE
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

package software.bigbade.enchantmenttokens.loader;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;
import software.bigbade.enchantmenttokens.utils.currency.EnchantCurrencyHandler;

import java.util.HashMap;
import java.util.Map;

public class GemCurrencyHandler extends EnchantCurrencyHandler {
    private final FileLoader fileLoader;
    @Getter
    private final Map<String, String> storedData = new HashMap<>();

    public GemCurrencyHandler(Player player, FileLoader fileLoader, SchedulerHandler scheduler) {
        super(player, "gemsOld");
        this.fileLoader = fileLoader;
        scheduler.runTaskAsync(() -> fileLoader.getData(player, this));
    }

    @Override
    public void savePlayer(EnchantmentPlayer player) {
        fileLoader.savePlayer(player, this);
    }

    @Override
    public void storePlayerData(NamespacedKey key, String value) {
        storedData.put(key.toString(), value);
    }

    @Override
    public String getPlayerData(NamespacedKey key) {
        return storedData.get(key.toString());
    }

    @Override
    public void removePlayerData(NamespacedKey key) {
        storedData.remove(key.toString());
    }

    protected void addRawData(String key, String value) {
        storedData.put(key, value);
    }
}
