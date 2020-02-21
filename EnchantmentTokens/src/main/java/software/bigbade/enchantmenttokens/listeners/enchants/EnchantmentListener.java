package software.bigbade.enchantmenttokens.listeners.enchants;

import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import org.bukkit.event.Event;

@FunctionalInterface
public interface EnchantmentListener<T extends EnchantmentEvent> {
    void apply(T event);
}
