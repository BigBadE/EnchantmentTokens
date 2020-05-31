/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.utils.players.PlayerHandler;

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
        EnchantmentPlayer enchantmentPlayer = playerHandler.getPlayer(player);
        enchantmentPlayer.save(true);
        playerHandler.removePlayer(enchantmentPlayer);
    }
}
