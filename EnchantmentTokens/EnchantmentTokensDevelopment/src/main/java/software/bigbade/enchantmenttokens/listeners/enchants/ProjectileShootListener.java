package software.bigbade.enchantmenttokens.listeners.enchants;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import software.bigbade.enchantmenttokens.api.EventFactory;
import software.bigbade.enchantmenttokens.api.ListenerType;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

public class ProjectileShootListener extends BasicEnchantListener implements Listener {
    private ListenerManager crossbowShoot;
    private ListenerManager tridentThrow;
    private ListenerManager bowShoot;
    private int version;

    public ProjectileShootListener(int version, ListenerManager crossbowShoot, ListenerManager tridentThrow, ListenerManager bowShoot) {
        this.version = version;
        this.crossbowShoot = crossbowShoot;
        this.tridentThrow = tridentThrow;
        this.bowShoot = bowShoot;
    }

    @EventHandler
    public void onProjectileShoot(ProjectileLaunchEvent event) {
        if(event.getEntity().getShooter() instanceof Player) {
            Player shooter = (Player) event.getEntity().getShooter();
            if (version > 14 && event.getEntityType() == EntityType.TRIDENT) {
                callListeners(EventFactory.createEvent(ListenerType.TRIDENT_THROW, null).setUser(shooter).setTargetEntity(event.getEntity()), tridentThrow);
            } else if(version > 13 && shooter.getInventory().getItemInMainHand().getType() == Material.CROSSBOW){
                callListeners(EventFactory.createEvent(ListenerType.CROSSBOW_SHOOT, shooter.getInventory().getItemInMainHand()).setUser(shooter).setTargetEntity(event.getEntity()), crossbowShoot);
            } else {
                callListeners(EventFactory.createEvent(ListenerType.SHOOT, shooter.getInventory().getItemInMainHand()).setUser(shooter).setTargetEntity(event.getEntity()), bowShoot);
            }
        }
    }
}