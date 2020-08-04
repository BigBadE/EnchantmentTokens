/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners.enchants;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import software.bigbade.enchantmenttokens.api.EventFactory;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

@RequiredArgsConstructor
public class FishingCatchEvent extends BasicEnchantListener<PlayerFishEvent> implements Listener {
    public FishingCatchEvent(ListenerManager<PlayerFishEvent> enchantListeners) {
        super(enchantListeners);
    }

    @EventHandler
    public void onPlayerCatch(PlayerFishEvent event) {
        callListeners(EventFactory.createCancellableEvent(event, event.getPlayer().getInventory().getItemInMainHand(), event.getPlayer()).setTargetEntity(event.getCaught()));
    }
}
