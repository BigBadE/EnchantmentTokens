/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners.enchants;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import software.bigbade.enchantmenttokens.api.EventFactory;
import software.bigbade.enchantmenttokens.api.ListenerType;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

public class FishingCatchEvent extends BasicEnchantListener implements Listener {
    public FishingCatchEvent(ListenerManager<?> enchantListeners) {
        super(enchantListeners);
    }

    @EventHandler
    public void onPlayerCatch(PlayerFishEvent event) {
        callListeners(new EventFactory<PlayerFishEvent>().createEvent(event, ListenerType.FISHING_ROD_CATCH, event.getPlayer().getInventory().getItemInMainHand(), event.getPlayer()).setTargetEntity(event.getCaught()));
    }
}
