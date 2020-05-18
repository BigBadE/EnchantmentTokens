/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners.enchants;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.EventFactory;
import software.bigbade.enchantmenttokens.api.ListenerType;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@RequiredArgsConstructor
public class ProjectileShootListener extends BasicEnchantListener implements Listener {
    private final EnchantmentTokens main;
    @Nullable
    private final ListenerManager<?> crossbowShoot;
    @Nullable
    private final ListenerManager<?> tridentThrow;
    @Nonnull
    private final ListenerManager<?> bowShoot;

    @EventHandler
    public void onProjectileShoot(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            Player shooter = (Player) event.getEntity().getShooter();
            event.getEntity().setMetadata("ce_firing_item", new FixedMetadataValue(main, shooter.getInventory().getItemInMainHand()));
            if (main.getVersion() > 14 && event.getEntityType() == EntityType.TRIDENT) {
                callListeners(new EventFactory<ProjectileLaunchEvent>().createEvent(event, ListenerType.TRIDENT_THROW, shooter.getInventory().getItemInMainHand(), shooter).setTargetEntity(event.getEntity()), tridentThrow);
            } else if (main.getVersion() > 13 && shooter.getInventory().getItemInMainHand().getType() == Material.CROSSBOW) {
                callListeners(new EventFactory<ProjectileLaunchEvent>().createEvent(event, ListenerType.CROSSBOW_SHOOT, shooter.getInventory().getItemInMainHand(), shooter).setTargetEntity(event.getEntity()), crossbowShoot);
            } else {
                callListeners(new EventFactory<ProjectileLaunchEvent>().createEvent(event, ListenerType.BOW_SHOOT, shooter.getInventory().getItemInMainHand(), shooter).setTargetEntity(event.getEntity()), bowShoot);
            }
        }
    }
}