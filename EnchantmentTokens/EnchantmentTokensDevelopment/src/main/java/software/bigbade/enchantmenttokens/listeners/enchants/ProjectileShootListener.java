package software.bigbade.enchantmenttokens.listeners.enchants;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import software.bigbade.enchantmenttokens.api.ListenerType;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

public class ProjectileShootListener extends BasicEnchantListener implements Listener {
    private ListenerManager projectileShoot;
    private ListenerManager tridentThrow;
    private int version;

    public ProjectileShootListener(int version, ListenerManager projectileShoot, ListenerManager tridentThrow) {
        this.version = version;
        this.projectileShoot = projectileShoot;
        this.tridentThrow = tridentThrow;
    }

    @EventHandler
    public void onProjectileShoot(ProjectileLaunchEvent event) {
        Player shooter = (Player) event.getEntity().getShooter();
        if(event.getEntity().getShooter() instanceof Player) {
            if (version > 14 && event.getEntityType() == EntityType.TRIDENT) {
                callListeners(new EnchantmentEvent(ListenerType.TRIDENT_THROW, null).setUser(shooter).setTargetEntity(event.getEntity()), tridentThrow);
            } else {
                callListeners(new EnchantmentEvent(ListenerType.SHOOT, shooter.getInventory().getItemInMainHand()).setUser(shooter).setTargetEntity(event.getEntity()), projectileShoot);
            }
        }
    }
}