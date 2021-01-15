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

package com.bigbade.enchantmenttokens.listeners.enchants;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;
import com.bigbade.enchantmenttokens.api.EventFactory;
import com.bigbade.enchantmenttokens.events.EnchantmentEvent;
import com.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

public class BlockDamageListener extends BasicEnchantListener<BlockDamageEvent> implements Listener {

    public BlockDamageListener(ListenerManager<BlockDamageEvent> eventListeners) {
        super(eventListeners);
    }

    @EventHandler
    public void blockBreakStart(BlockDamageEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(item.getType() != Material.AIR) {
            EnchantmentEvent<BlockDamageEvent> enchantmentEvent = EventFactory.createCancellableEvent(event, item, event.getPlayer()).setTargetBlock(event.getBlock());
            callListeners(enchantmentEvent);
        }
    }
}
