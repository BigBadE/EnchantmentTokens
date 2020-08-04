/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners.enchants;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import software.bigbade.enchantmenttokens.api.EventFactory;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

@RequiredArgsConstructor
public class PotionListener extends BasicEnchantListener<EntityPotionEffectEvent> implements Listener {
    private final ListenerManager<EntityPotionEffectEvent> potionAdd;
    private final ListenerManager<EntityPotionEffectEvent> potionRemove;

    @EventHandler
    public void onPotionApply(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        Player player = (Player) event.getEntity();

        if (event.getAction().equals(EntityPotionEffectEvent.Action.ADDED)) {
            callForAllItems(potionAdd, EventFactory.createCancellableEvent(event, null, player));
        } else if (event.getAction().equals(EntityPotionEffectEvent.Action.REMOVED) || event.getAction().equals(EntityPotionEffectEvent.Action.CLEARED)) {
            callForAllItems(potionRemove, EventFactory.createCancellableEvent(event, null, player));
        }
    }
}
