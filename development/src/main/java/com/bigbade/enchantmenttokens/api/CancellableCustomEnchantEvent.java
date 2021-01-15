/*
 * Custom enchantments for Minecraft
 * Copyright (C) 2021 Big_Bad_E
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bigbade.enchantmenttokens.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import com.bigbade.enchantmenttokens.events.CancellableEnchantmentEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CancellableCustomEnchantEvent<T extends Event & Cancellable> extends CustomEnchantmentEvent<T> implements CancellableEnchantmentEvent<T> {
    public CancellableCustomEnchantEvent(@Nonnull T event, @Nullable ItemStack item, @Nonnull Player user) {
        super(event, item, user);
    }

    @Override
    public boolean isCancelled() {
        return getEvent().isCancelled();
    }

    public void setCancelled(boolean cancelled) {
        getEvent().setCancelled(cancelled);
    }
}
