/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners.enchants;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.api.EventFactory;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.utils.ReflectionManager;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

@RequiredArgsConstructor
public class PlayerDamageListener extends BasicEnchantListener<EntityDamageByEntityEvent> implements Listener {
    private final ListenerManager<EntityDamageByEntityEvent> hit;
    private final ListenerManager<EntityDamageByEntityEvent> damage;
    private final ListenerManager<EntityDamageByEntityEvent> block;
    private final ListenerManager<EntityDamageByEntityEvent> swordBlock;
    private final ListenerManager<EntityDamageByEntityEvent> death;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.isBlocking()) {
                onPlayerBlock(player, event);
            }
            EnchantmentEvent<EntityDamageByEntityEvent> enchantmentEvent = EventFactory.createEvent(event, player.getInventory().getItemInMainHand(), player).setTargetEntity(event.getDamager());
            callListeners(enchantmentEvent, hit);
            if(((LivingEntity) event.getEntity()).getHealth() - event.getFinalDamage() <= 0) {
                callForAllItems(death, enchantmentEvent);
            }
        }
        if (event.getDamager() instanceof Player) {
            Player target = (Player) event.getDamager();
            EnchantmentEvent<EntityDamageByEntityEvent> damageEvent = EventFactory.createEvent(event, target.getInventory().getItemInMainHand(), target).setTargetEntity(event.getEntity());
            callListeners(damageEvent, damage);
        }
    }

    private void onPlayerBlock(Player player, EntityDamageByEntityEvent event) {
        ItemStack blocking = null;
        if(ReflectionManager.VERSION >= 9) {
            if(player.getInventory().getItemInMainHand().getType() == Material.SHIELD) {
                blocking = player.getInventory().getItemInMainHand();
            } else if(player.getInventory().getItemInOffHand().getType() == Material.SHIELD) {
                blocking = player.getInventory().getItemInOffHand();
            }
            EnchantmentEvent<EntityDamageByEntityEvent> enchantmentEvent = EventFactory.createEvent(event, blocking, player).setTargetEntity(event.getDamager());
            callListeners(enchantmentEvent, block);
        } else {
            blocking = player.getInventory().getItemInMainHand();
            EnchantmentEvent<EntityDamageByEntityEvent> enchantmentEvent = EventFactory.createEvent(event, blocking, player).setTargetEntity(event.getDamager());
            callListeners(enchantmentEvent, swordBlock);
        }
    }
}
