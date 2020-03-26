package software.bigbade.enchantmenttokens.utils.listeners;

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
        listeners.forEach((enchant, listener) -> {
            if (enchant.equals(base))
                listener.apply(event);
        });
    }

    public void callEvent(EnchantmentEvent event) {
        event.getItem().getEnchantments().keySet().stream()
                .filter(enchantment -> enchantment instanceof EnchantmentBase)
                .forEach(enchantment -> listeners.get(enchantment).apply(event));
    }
}
