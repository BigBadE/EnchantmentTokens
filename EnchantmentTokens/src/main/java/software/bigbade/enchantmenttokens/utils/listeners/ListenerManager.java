package software.bigbade.enchantmenttokens.utils.listeners;

import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.events.CustomEnchantEvent;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.listeners.enchants.EnchantmentListener;

import java.util.HashMap;
import java.util.Map;

public class ListenerManager {
    private Map<EnchantmentBase, EnchantmentListener<CustomEnchantEvent>> listeners = new HashMap<>();

    public void add(EnchantmentListener<CustomEnchantEvent> listener, EnchantmentBase base) {
        listeners.put(base, listener);
    }

    public Map<EnchantmentBase, EnchantmentListener<CustomEnchantEvent>> getListeners() {
        return listeners;
    }

    public void callEvent(CustomEnchantEvent event, EnchantmentBase base) {
        listeners.forEach((enchant, listener) -> {
            if (enchant.equals(base))
                listener.apply(event);
        });
    }

    public void callEvent(CustomEnchantEvent event) {
        event.getItem().getEnchantments().keySet().stream()
                .filter(enchantment -> enchantment instanceof EnchantmentBase)
                .forEach(enchantment -> listeners.get(enchantment).apply(event));
    }
}
