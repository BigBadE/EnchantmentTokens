package software.bigbade.enchantmenttokens.listeners.enchants;

import software.bigbade.enchantmenttokens.events.EnchantmentEvent;

@FunctionalInterface
public interface EnchantmentListener<T extends EnchantmentEvent> {
    void apply(T event);
}
