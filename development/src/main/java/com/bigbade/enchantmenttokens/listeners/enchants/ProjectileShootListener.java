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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;
import com.bigbade.enchantmenttokens.EnchantmentTokens;
import com.bigbade.enchantmenttokens.api.EventFactory;
import com.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@RequiredArgsConstructor
public class ProjectileShootListener extends BasicEnchantListener<ProjectileLaunchEvent> implements Listener {
    private final EnchantmentTokens main;
    @Nullable
    private final ListenerManager<ProjectileLaunchEvent> crossbowShoot;
    @Nullable
    private final ListenerManager<ProjectileLaunchEvent> tridentThrow;
    @Nonnull
    private final ListenerManager<ProjectileLaunchEvent> bowShoot;

    @EventHandler
    public void onProjectileShoot(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            Player shooter = (Player) event.getEntity().getShooter();
            event.getEntity().setMetadata("ce_firing_item", new FixedMetadataValue(main, shooter.getInventory().getItemInMainHand()));
            if (tridentThrow != null && event.getEntityType() == EntityType.TRIDENT) {
                callListeners(EventFactory.createCancellableEvent(event, shooter.getInventory().getItemInMainHand(), shooter).setTargetEntity(event.getEntity()), tridentThrow);
            } else if (crossbowShoot != null && shooter.getInventory().getItemInMainHand().getType() == Material.CROSSBOW) {
                callListeners(EventFactory.createCancellableEvent(event, shooter.getInventory().getItemInMainHand(), shooter).setTargetEntity(event.getEntity()), crossbowShoot);
            } else {
                callListeners(EventFactory.createCancellableEvent(event, shooter.getInventory().getItemInMainHand(), shooter).setTargetEntity(event.getEntity()), bowShoot);
            }
        }
    }
}
