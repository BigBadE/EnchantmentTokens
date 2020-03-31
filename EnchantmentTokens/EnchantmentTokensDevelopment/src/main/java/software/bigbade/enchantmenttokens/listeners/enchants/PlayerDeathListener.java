/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners.enchants;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import software.bigbade.enchantmenttokens.api.EventFactory;
import software.bigbade.enchantmenttokens.api.ListenerType;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

public class PlayerDeathListener extends BasicEnchantListener implements Listener {
    public PlayerDeathListener(ListenerManager enchantListeners) {
        super(enchantListeners);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        callForAllItems(event.getEntity(), EventFactory.createEvent(ListenerType.DEATH, null).setUser(event.getEntity()));
    }
}
