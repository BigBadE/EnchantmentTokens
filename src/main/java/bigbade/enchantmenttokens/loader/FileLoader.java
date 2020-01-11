package bigbade.enchantmenttokens.loader;

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
import bigbade.enchantmenttokens.api.EnchantmentPlayer;
import bigbade.enchantmenttokens.utils.ByteUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FileLoader {
    private EnchantmentTokens main;
    private ByteUtils utils = new ByteUtils();
    private List<EnchantmentPlayer> cache = new ArrayList<>();

    public FileLoader(EnchantmentTokens main) {
        this.main = main;
    }

    public EnchantmentPlayer loadPlayer(Player player) {
        for (EnchantmentPlayer player1 : cache)
            if (player1.getPlayer() == player)
                return player1;
        if (cache.size() > 50) saveCache();
        EnchantmentPlayer player1 = EnchantmentPlayer.loadPlayer(player, main, utils);
        cache.add(player1);
        return player1;
    }

    public void savePlayer(EnchantmentPlayer player, boolean remove) {
        player.save(utils, main);
        if(remove)
            cache.remove(player);
    }

    public void saveCache() {
        EnchantmentTokens.LOGGER.info("Saving player cache!");
        for (EnchantmentPlayer player : cache)
            savePlayer(player, false);
        cache.clear();
    }
}