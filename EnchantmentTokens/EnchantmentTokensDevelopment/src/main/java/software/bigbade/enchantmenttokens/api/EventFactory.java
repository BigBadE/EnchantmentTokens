/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.api;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;

public class EventFactory<T extends Event> {
    public EnchantmentEvent<T> createEvent(T event, ListenerType type, ItemStack item, Entity user) {
        return new CustomEnchantmentEvent<>(event, type, item, user);
    }
}
