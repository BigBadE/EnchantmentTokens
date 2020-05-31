/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners.enchants;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.api.EventFactory;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

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
