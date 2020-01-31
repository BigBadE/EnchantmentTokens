package bigbade.enchantmenttokens.listeners;

import bigbade.enchantmenttokens.EnchantmentTokens;
import bigbade.enchantmenttokens.api.EnchantmentPlayer;
import bigbade.enchantmenttokens.utils.EnchantmentPlayerHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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

public class PlayerLeaveListener implements Listener {
    private EnchantmentPlayerHandler playerHandler;

    public PlayerLeaveListener(EnchantmentTokens main) {
        this.playerHandler = main.getPlayerHandler();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        savePlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        savePlayer(event.getPlayer());
    }

    private void savePlayer(Player player) {
        EnchantmentPlayer enchantmentPlayer = playerHandler.getPlayer(player);
        enchantmentPlayer.save();
        playerHandler.removePlayer(enchantmentPlayer);
    }
}
