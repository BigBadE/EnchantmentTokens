package software.bigbade.enchantmenttokens.utils.listeners;

import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.listeners.enchants.EnchantmentListener;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import software.bigbade.enchantmenttokens.skript.SkriptEnchantment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListenerManager {
    private Map<Object, EnchantmentListener<EnchantmentEvent>> listeners = new HashMap<>();
    private List<SkriptEnchantment> skriptListeners = new ArrayList<>();

    public void add(EnchantmentListener<EnchantmentEvent> listener, Object base) {
        listeners.put(base, listener);
    }

    public void callEvent(EnchantmentEvent event, EnchantmentBase base) {
        for (Map.Entry<Object, EnchantmentListener<EnchantmentEvent>> listenerEntry : listeners.entrySet()) {
            if(listenerEntry.getKey().equals(base)) {
                listenerEntry.getValue().apply(event);
            }
        }
    }

    public void callEvent(EnchantmentEvent event) {
        for (Map.Entry<Object, EnchantmentListener<EnchantmentEvent>> listenerEntry : listeners.entrySet()) {
            if (listenerEntry.getKey() instanceof EnchantmentBase) {
                for (Enchantment enchantment : event.getItem().getEnchantments().keySet()) {
                    if (enchantment.getKey().equals(((EnchantmentBase) listenerEntry.getKey()).getKey()))
                        listenerEntry.getValue().apply(event);
                }
            } else {
                listenerEntry.getValue().apply(event);
            }
        }
    }

    public boolean hasListener(SkriptEnchantment enchantment) {
        return skriptListeners.contains(enchantment);
    }

    public void addSkriptListener(SkriptEnchantment enchantment) {
        skriptListeners.add(enchantment);
    }
}
