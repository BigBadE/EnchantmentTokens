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
import software.bigbade.enchantmenttokens.api.ListenerType;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

<<<<<<< HEAD:EnchantmentTokens/EnchantmentTokensDevelopment/src/main/java/software/bigbade/enchantmenttokens/listeners/enchants/PotionListener.java
@RequiredArgsConstructor
public class PotionListener extends BasicEnchantListener<EntityPotionEffectEvent> implements Listener {
    private final ListenerManager<EntityPotionEffectEvent> potionAdd;
    private final ListenerManager<EntityPotionEffectEvent> potionRemove;
=======
public class PotionListener extends BasicEnchantListener implements Listener {
    private final ListenerManager potionAdd;
    private final ListenerManager potionRemove;

    public PotionListener(ListenerManager potionAdd, ListenerManager potionRemove) {
        super(null);
        this.potionAdd = potionAdd;
        this.potionRemove = potionRemove;
    }
>>>>>>> 3d705af96ebb617ac55d44878c2077b5e14535b9:EnchantmentTokensMain/EnchantmentTokensDevelopment/src/main/java/software/bigbade/enchantmenttokens/listeners/enchants/PotionListener.java

    @EventHandler
    public void onPotionApply(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        Player player = (Player) event.getEntity();

        if (event.getAction().equals(EntityPotionEffectEvent.Action.ADDED)) {
            callForAllItems(potionAdd, EventFactory.createEvent(event, null, player));
        } else if (event.getAction().equals(EntityPotionEffectEvent.Action.REMOVED) || event.getAction().equals(EntityPotionEffectEvent.Action.CLEARED)) {
            callForAllItems(potionRemove, EventFactory.createEvent(event, null, player));
        }
    }
}
