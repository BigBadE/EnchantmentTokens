package bigbade.enchantmenttokens.listeners.enchants;

import bigbade.enchantmenttokens.api.EnchantmentBase;
import bigbade.enchantmenttokens.events.EnchantmentEvent;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/*
EnchantmentTokens
Copyright (C) 2019-2020 Big_Bad_E
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

public class BasicEnchantListener<T extends Event> {
    private Map<EnchantmentBase, EnchantmentListener<EnchantmentEvent<T>>> listeners;

    public BasicEnchantListener(Map<EnchantmentBase, EnchantmentListener<EnchantmentEvent<T>>> listeners) {
        this.listeners = listeners;
    }

    public void callListeners(ItemStack item, EnchantmentEvent<T> event) {
        for (Map.Entry<EnchantmentBase, EnchantmentListener<EnchantmentEvent<T>>> enchantment : listeners.entrySet()) {
            if (item.containsEnchantment(enchantment.getKey())) {
                enchantment.getValue().apply(event);
            }
        }
    }

    public void callListeners(ItemStack item, EnchantmentEvent<T> event, Map<EnchantmentBase, EnchantmentListener<EnchantmentEvent<T>>> listeners) {
        for (Map.Entry<EnchantmentBase, EnchantmentListener<EnchantmentEvent<T>>> enchantment : listeners.entrySet()) {
            if (item.containsEnchantment(enchantment.getKey())) {
                enchantment.getValue().apply(event);
            }
        }
    }
}
