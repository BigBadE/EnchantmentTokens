/*
 * Addons for the Custom Enchantment API in Minecraft
 * Copyright (C) 2020 BigBadE
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

package software.bigbade.enchantmenttokens.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface EnchantmentEvent {
    @Nonnull
    EnchantmentEvent setUser(Entity user);

    @Nonnull
    Entity getUser();

    @Nonnull
    EnchantmentEvent setItem(ItemStack item);

    @Nonnull
    ItemStack getItem();

    @Nonnull
    EnchantmentEvent setTargetEntity(Entity entity);

    @Nullable
    Entity getTargetEntity();

    @Nonnull
    EnchantmentEvent setTargetBlock(Block block);

    @Nullable
    Block getTargetBlock();

    Event getEvent();
}
