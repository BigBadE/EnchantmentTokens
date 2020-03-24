package software.bigbade.enchantmenttokens.utils.listeners;

import software.bigbade.enchantmenttokens.events.EnchantmentEvent;

@FunctionalInterface
public interface EnchantmentListener<T extends EnchantmentEvent> {
    void apply(T event);
}
