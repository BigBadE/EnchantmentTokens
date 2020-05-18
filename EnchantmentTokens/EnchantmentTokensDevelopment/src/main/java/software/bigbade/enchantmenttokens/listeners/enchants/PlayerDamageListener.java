/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners.enchants;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import software.bigbade.enchantmenttokens.api.EventFactory;
import software.bigbade.enchantmenttokens.api.ListenerType;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

import javax.annotation.Nullable;

@RequiredArgsConstructor
public class PlayerDamageListener extends BasicEnchantListener implements Listener {
    private final ListenerManager hit;
    private final ListenerManager damage;

    //Not in 1.8, so can be null.
    @Nullable
    private final ListenerManager block;

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (block != null && player.isBlocking()) {
                EnchantmentEvent<EntityDamageByEntityEvent> enchantmentEvent = new EventFactory<EntityDamageByEntityEvent>().createEvent(event, ListenerType.SHIELD_BLOCK, player.getInventory().getItemInMainHand(), player).setTargetEntity(event.getDamager());
                callListeners(enchantmentEvent, block);
            }
            EnchantmentEvent<EntityDamageByEntityEvent> enchantmentEvent = new EventFactory<EntityDamageByEntityEvent>().createEvent(event, ListenerType.ON_DAMAGED, player.getInventory().getItemInMainHand(), player).setTargetEntity(event.getDamager());
            callListeners(enchantmentEvent, hit);
        }
        if (event.getDamager() instanceof Player) {
            Player target = (Player) event.getDamager();
            EnchantmentEvent<EntityDamageByEntityEvent> damageEvent = new EventFactory<EntityDamageByEntityEvent>().createEvent(event, ListenerType.ENTITY_DAMAGED, target.getInventory().getItemInMainHand(), target).setTargetEntity(event.getEntity());
            callListeners(damageEvent, damage);
        }
    }
}
