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
import software.bigbade.enchantmenttokens.api.ListenerType;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@RequiredArgsConstructor
public class ProjectileHitListener extends BasicEnchantListener implements Listener {
    private final int version;
    @Nullable
    private final ListenerManager<?> tridentHit;
    @Nonnull
    private final ListenerManager<?> arrowHit;

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            Player shooter = (Player) event.getEntity().getShooter();
            ItemStack item = (ItemStack) event.getEntity().getMetadata("ce_firing_item").get(0).value();
            if (version > 14 && event.getEntityType() == EntityType.TRIDENT) {
                callListeners(new EventFactory<ProjectileHitEvent>().createEvent(event, ListenerType.TRIDENT_HIT, item, shooter).setTargetEntity(event.getHitEntity()), tridentHit);
            } else {
                callListeners(new EventFactory<ProjectileHitEvent>().createEvent(event, ListenerType.ARROW_HIT, item, shooter).setTargetEntity(event.getHitEntity()), arrowHit);
            }
        }
    }
}
