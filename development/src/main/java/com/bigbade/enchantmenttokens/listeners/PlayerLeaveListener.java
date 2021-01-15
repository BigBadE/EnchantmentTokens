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

package com.bigbade.enchantmenttokens.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import com.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import com.bigbade.enchantmenttokens.api.wrappers.EnchantmentChain;
import com.bigbade.enchantmenttokens.utils.players.PlayerHandler;

public class PlayerLeaveListener implements Listener {
    private final PlayerHandler playerHandler;

    public PlayerLeaveListener(PlayerHandler handler) {
        this.playerHandler = handler;
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
        new EnchantmentChain<>(player.getUniqueId().toString()).async(() -> {
            EnchantmentPlayer enchantmentPlayer = playerHandler.getPlayer(player);
            playerHandler.removePlayer(enchantmentPlayer);
        }).execute();
    }
}
