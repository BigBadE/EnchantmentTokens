package bigbade.enchantmenttokens.utils;

import bigbade.enchantmenttokens.api.EnchantmentPlayer;
import bigbade.enchantmenttokens.utils.currency.CurrencyHandler;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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

public class EnchantmentPlayerHandler {
    private List<EnchantmentPlayer> players = new ArrayList<>();

    public EnchantmentPlayer loadPlayer(Player player, CurrencyHandler handler) {
        EnchantmentPlayer enchantmentPlayer = EnchantmentPlayer.loadPlayer(player);
        enchantmentPlayer.setHandler(handler);
        players.add(enchantmentPlayer);
        return enchantmentPlayer;
    }

    public EnchantmentPlayer getPlayer(Player player, CurrencyHandler handler) {
        for (EnchantmentPlayer enchantmentPlayer : players)
            if (enchantmentPlayer.getPlayer().getUniqueId().equals(player.getUniqueId()))
                return enchantmentPlayer;
        return loadPlayer(player, handler);
    }

    public void removePlayer(EnchantmentPlayer player) {
        System.out.println("Safely removed");
        players.remove(player);
    }
}
