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
import software.bigbade.enchantmenttokens.utils.ReflectionManager;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

<<<<<<< HEAD:EnchantmentTokens/EnchantmentTokensDevelopment/src/main/java/software/bigbade/enchantmenttokens/listeners/enchants/ProjectileShootListener.java
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
=======
public class ProjectileShootListener extends BasicEnchantListener implements Listener {
    private final ListenerManager crossbowShoot;
    private final ListenerManager tridentThrow;
    private final ListenerManager bowShoot;
    private final int version;
>>>>>>> 3d705af96ebb617ac55d44878c2077b5e14535b9:EnchantmentTokensMain/EnchantmentTokensDevelopment/src/main/java/software/bigbade/enchantmenttokens/listeners/enchants/ProjectileShootListener.java

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
                callListeners(EventFactory.createEvent(event, shooter.getInventory().getItemInMainHand(), shooter).setTargetEntity(event.getEntity()), tridentThrow);
            } else if (crossbowShoot != null && shooter.getInventory().getItemInMainHand().getType() == Material.CROSSBOW) {
                callListeners(EventFactory.createEvent(event, shooter.getInventory().getItemInMainHand(), shooter).setTargetEntity(event.getEntity()), crossbowShoot);
            } else {
                callListeners(EventFactory.createEvent(event, shooter.getInventory().getItemInMainHand(), shooter).setTargetEntity(event.getEntity()), bowShoot);
            }
        }
    }
}