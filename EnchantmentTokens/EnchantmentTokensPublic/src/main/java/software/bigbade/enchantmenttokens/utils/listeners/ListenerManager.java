package software.bigbade.enchantmenttokens.utils.listeners;

import org.bukkit.enchantments.Enchantment;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;

import java.util.HashMap;
import java.util.Map;

public class ListenerManager {
    private Map<EnchantmentBase, EnchantmentListener<EnchantmentEvent>> listeners = new HashMap<>();

    public void add(EnchantmentListener<EnchantmentEvent> listener, EnchantmentBase base) {
        listeners.put(base, listener);
    }

    public Map<EnchantmentBase, EnchantmentListener<EnchantmentEvent>> getListeners() {
        return listeners;
    }

    public void callEvent(EnchantmentEvent event, EnchantmentBase base) {
        for (Map.Entry<EnchantmentBase, EnchantmentListener<EnchantmentEvent>> listenerEntry : listeners.entrySet()) {
            if (listenerEntry.getKey().equals(base)) {
                listenerEntry.getValue().apply(event);
            }
        }
    }

    public void callEvent(EnchantmentEvent event) {
        listeners.forEach((base, listener) -> {
            for (Enchantment enchantment : event.getItem().getEnchantments().keySet()) {
                if (enchantment.getKey().equals(base.getKey()))
                    listener.apply(event);
            }
        });
    }
}
