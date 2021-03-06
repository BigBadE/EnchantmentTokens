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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import com.bigbade.enchantmenttokens.api.EventFactory;
import com.bigbade.enchantmenttokens.events.EnchantmentEvent;
import com.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@RequiredArgsConstructor
public class ProjectileHitListener extends BasicEnchantListener<ProjectileHitEvent> implements Listener {
    @Nullable
    private final ListenerManager<ProjectileHitEvent> tridentHit;
    @Nonnull
    private final ListenerManager<ProjectileHitEvent> arrowHit;

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            Player shooter = (Player) event.getEntity().getShooter();
            ItemStack item = (ItemStack) event.getEntity().getMetadata("ce_firing_item").get(0).value();
            EnchantmentEvent<ProjectileHitEvent> enchantmentEvent = EventFactory.createEvent(event, item, shooter).setTargetEntity(event.getHitEntity());
            if (tridentHit != null && event.getEntityType() == EntityType.TRIDENT) {
                callListeners(enchantmentEvent, tridentHit);
            } else {
                callListeners(enchantmentEvent, arrowHit);
            }
        }
    }
}
