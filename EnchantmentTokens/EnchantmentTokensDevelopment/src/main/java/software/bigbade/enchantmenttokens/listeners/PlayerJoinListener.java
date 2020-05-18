/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import software.bigbade.enchantmenttokens.utils.players.PlayerHandler;

public class PlayerJoinListener implements Listener {

    private final PlayerHandler playerHandler;

    public PlayerJoinListener(PlayerHandler handler) {
        playerHandler = handler;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        playerHandler.loadPlayer(event.getPlayer());
    }
}
