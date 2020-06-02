/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;

public final class EventFactory {
    private EventFactory() {
    }

    public static <T extends Event> EnchantmentEvent<T> createEvent(T event, ItemStack item, Player user) {
        return new CustomEnchantmentEvent<>(event, item, user);
    }

    public static <T extends Event & Cancellable> EnchantmentEvent<T> createCancellableEvent(T event, ItemStack item, Player user) {
        return new CancellableCustomEnchantEvent<>(event, item, user);
    }
}
