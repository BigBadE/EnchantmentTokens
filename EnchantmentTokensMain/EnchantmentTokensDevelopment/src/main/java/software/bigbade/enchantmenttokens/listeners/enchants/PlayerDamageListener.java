/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners.enchants;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import software.bigbade.enchantmenttokens.api.EventFactory;
import software.bigbade.enchantmenttokens.api.ListenerType;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

import javax.annotation.Nullable;

public class PlayerDamageListener extends BasicEnchantListener implements Listener {
    private final ListenerManager hit;
    private final ListenerManager block;

    public PlayerDamageListener(ListenerManager hit, @Nullable ListenerManager block) {
        this.hit = hit;
        this.block = block;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if(block != null && player.isBlocking()) {
                EnchantmentEvent enchantmentEvent = EventFactory.createEvent(ListenerType.SHIELD_BLOCK, player.getInventory().getItemInMainHand()).setUser(player).setTargetEntity(event.getDamager());
                callListeners(enchantmentEvent, block);
            }
            EnchantmentEvent enchantmentEvent = EventFactory.createEvent(ListenerType.DAMAGE, player.getInventory().getItemInMainHand()).setUser(player).setTargetEntity(event.getDamager());
            callListeners(enchantmentEvent, hit);
        }
    }
}
