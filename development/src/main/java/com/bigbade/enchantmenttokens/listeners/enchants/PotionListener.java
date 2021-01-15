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

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import com.bigbade.enchantmenttokens.api.EventFactory;
import com.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

@RequiredArgsConstructor
public class PotionListener extends BasicEnchantListener<EntityPotionEffectEvent> implements Listener {
    private final ListenerManager<EntityPotionEffectEvent> potionAdd;
    private final ListenerManager<EntityPotionEffectEvent> potionRemove;

    @EventHandler
    public void onPotionApply(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();

        if (event.getAction().equals(EntityPotionEffectEvent.Action.ADDED)) {
            callForAllItems(potionAdd, EventFactory.createCancellableEvent(event, null, player));
        } else if (event.getAction().equals(EntityPotionEffectEvent.Action.REMOVED) || event.getAction().equals(EntityPotionEffectEvent.Action.CLEARED)) {
            callForAllItems(potionRemove, EventFactory.createCancellableEvent(event, null, player));
        }
    }
}
