package software.bigbade.enchantmenttokens.listeners.enchants;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import software.bigbade.enchantmenttokens.api.ListenerType;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.events.EventFactory;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

public class DamageListener extends BasicEnchantListener implements Listener {
    private ListenerManager hit;
    private ListenerManager block;

    public DamageListener(ListenerManager hit, ListenerManager block) {
        this.hit = hit;
        this.block = block;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if(((Player) event.getEntity()).isBlocking()) {
                EnchantmentEvent enchantmentEvent = EventFactory.createEvent(ListenerType.SHIELD_BLOCK, player.getInventory().getItemInMainHand()).setUser(player).setTargetEntity(event.getDamager());
                callListeners(enchantmentEvent, block);
            }
            EnchantmentEvent enchantmentEvent = EventFactory.createEvent(ListenerType.DAMAGE, player.getInventory().getItemInMainHand()).setUser(player).setTargetEntity(event.getDamager());
            callListeners(enchantmentEvent, hit);
        }
    }
}
