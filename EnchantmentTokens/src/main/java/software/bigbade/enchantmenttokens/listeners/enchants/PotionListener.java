package software.bigbade.enchantmenttokens.listeners.enchants;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import software.bigbade.enchantmenttokens.api.ListenerType;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

public class PotionListener extends BasicEnchantListener implements Listener {
    private ListenerManager potionAdd;
    private ListenerManager potionRemove;

    public PotionListener(ListenerManager potionAdd, ListenerManager potionRemove) {
        super(null);
        this.potionAdd = potionAdd;
        this.potionRemove = potionRemove;
    }

    @EventHandler
    public void onPotionApply(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        Player player = (Player) event.getEntity();

        if (event.getAction().equals(EntityPotionEffectEvent.Action.ADDED)) {
            callForAllItems(player, potionAdd, new EnchantmentEvent(ListenerType.POTION_APPLY, null).setUser(player));
        } else if (event.getAction().equals(EntityPotionEffectEvent.Action.REMOVED) || event.getAction().equals(EntityPotionEffectEvent.Action.CLEARED)) {
            callForAllItems(player, potionRemove, new EnchantmentEvent(ListenerType.POTION_REMOVE, null).setUser(player));
        }
    }
}