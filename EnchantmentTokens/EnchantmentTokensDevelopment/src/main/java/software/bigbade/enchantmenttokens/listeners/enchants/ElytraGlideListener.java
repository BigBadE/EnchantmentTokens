/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners.enchants;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import software.bigbade.enchantmenttokens.api.EventFactory;
import software.bigbade.enchantmenttokens.api.ListenerType;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

public class ElytraGlideListener extends BasicEnchantListener implements Listener {
    public ElytraGlideListener(ListenerManager<?> enchantListeners) {
        super(enchantListeners);
    }

    @EventHandler
    public void onPlayerDeath(EntityToggleGlideEvent event) {
        callListeners(new EventFactory<EntityToggleGlideEvent>().createEvent(event, ListenerType.ELYTRA_GLIDE, null, event.getEntity()));
    }
}
