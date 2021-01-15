/*
 * Custom enchantments for Minecraft
 * Copyright (C) 2021 Big_Bad_E
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bigbade.enchantmenttokens.listeners.enchants;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import com.bigbade.enchantmenttokens.api.EventFactory;
import com.bigbade.enchantmenttokens.events.EnchantmentEvent;
import com.bigbade.enchantmenttokens.utils.ReflectionManager;
import com.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

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
            EnchantmentEvent<EntityDamageByEntityEvent> enchantmentEvent = EventFactory.createCancellableEvent(event, player.getInventory().getItemInMainHand(), player).setTargetEntity(event.getDamager());
            callListeners(enchantmentEvent, hit);
            if(((LivingEntity) event.getEntity()).getHealth() - event.getFinalDamage() <= 0) {
                callForAllItems(death, enchantmentEvent);
            }
        }
        if (event.getDamager() instanceof Player) {
            Player target = (Player) event.getDamager();
            EnchantmentEvent<EntityDamageByEntityEvent> damageEvent = EventFactory.createCancellableEvent(event, target.getInventory().getItemInMainHand(), target).setTargetEntity(event.getEntity());
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
            EnchantmentEvent<EntityDamageByEntityEvent> enchantmentEvent = EventFactory.createCancellableEvent(event, blocking, player).setTargetEntity(event.getDamager());
            callListeners(enchantmentEvent, block);
        } else {
            blocking = player.getInventory().getItemInMainHand();
            EnchantmentEvent<EntityDamageByEntityEvent> enchantmentEvent = EventFactory.createCancellableEvent(event, blocking, player).setTargetEntity(event.getDamager());
            callListeners(enchantmentEvent, swordBlock);
        }
    }
}
