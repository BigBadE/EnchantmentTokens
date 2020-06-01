/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners.enchants;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRiptideEvent;
import software.bigbade.enchantmenttokens.api.EventFactory;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

public class RiptideListener extends BasicEnchantListener<PlayerRiptideEvent> implements Listener {
    public RiptideListener(ListenerManager<PlayerRiptideEvent> listeners) {
        super(listeners);
    }

    @EventHandler
    public void onRiptide(PlayerRiptideEvent event) {
        callListeners(EventFactory.createEvent(event, event.getItem(), event.getPlayer()));
    }
}