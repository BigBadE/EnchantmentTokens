/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners.enchants;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import software.bigbade.enchantmenttokens.api.EventFactory;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

public class PlayerDeathListener extends BasicEnchantListener<PlayerDeathEvent> implements Listener {
    public PlayerDeathListener(ListenerManager<PlayerDeathEvent> listeners) {
        super(listeners);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        callForAllItems(EventFactory.createEvent(event, null, event.getEntity()).setTargetEntity(event.getEntity().getKiller()));
    }
}
