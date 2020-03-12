package software.bigbade.enchantmenttokens.listeners.enchants;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRiptideEvent;
import software.bigbade.enchantmenttokens.api.ListenerType;
import software.bigbade.enchantmenttokens.events.CustomEnchantEvent;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

public class RiptideListener extends BasicEnchantListener implements Listener {
    public RiptideListener(ListenerManager listeners) {
        super(listeners);
    }

    @EventHandler
    public void onRiptide(PlayerRiptideEvent event) {
        callListeners(new CustomEnchantEvent(ListenerType.RIPTIDE, event.getItem()).setUser(event.getPlayer()));
    }
}
