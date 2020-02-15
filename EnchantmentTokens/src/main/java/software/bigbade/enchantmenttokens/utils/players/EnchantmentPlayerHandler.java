package software.bigbade.enchantmenttokens.utils.players;

import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;
import software.bigbade.enchantmenttokens.utils.currency.CurrencyFactory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    private CurrencyFactory currencyFactory;

    public EnchantmentPlayerHandler(CurrencyFactory currencyFactory) {
        this.currencyFactory = currencyFactory;
    }

    public EnchantmentPlayer loadPlayer(Player player) {
        EnchantmentPlayer enchantmentPlayer = EnchantmentPlayer.loadPlayer(player);
        enchantmentPlayer.setHandler(currencyFactory.newInstance(player));
        players.add(enchantmentPlayer);
        return enchantmentPlayer;
    }

    public EnchantmentPlayer getPlayer(Player player) {
        for (EnchantmentPlayer enchantmentPlayer : players)
            if (enchantmentPlayer.getPlayer().getUniqueId().equals(player.getUniqueId()))
                return enchantmentPlayer;
        return loadPlayer(player);
    }

    public void removePlayer(EnchantmentPlayer player) {
        players.remove(player);
    }

    public void autosave(SchedulerHandler handler) {
        if (!players.isEmpty()) {
            AtomicInteger saving = new AtomicInteger(0);
            handler.runTaskRepeating(() -> {
                if (saving.get() > players.size()) players.get(saving.getAndIncrement()).save();
            }, 0, 5);
        }
    }

    public void shutdown() {
        players.forEach(EnchantmentPlayer::save);
    }
}
