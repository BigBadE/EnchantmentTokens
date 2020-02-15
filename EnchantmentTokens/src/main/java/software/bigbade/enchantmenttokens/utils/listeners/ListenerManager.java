package software.bigbade.enchantmenttokens.utils.listeners;

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

import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.listeners.enchants.EnchantmentListener;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.Map;

public class ListenerManager {
    private Map<Object, EnchantmentListener<EnchantmentEvent>> listeners = new HashMap<>();

    public void add(EnchantmentListener<EnchantmentEvent> listener, Object base) {
        listeners.put(base, listener);
    }

    public void callEvent(EnchantmentEvent event, EnchantmentBase base) {
        event.setEnchantment(base);
        for (Map.Entry<Object, EnchantmentListener<EnchantmentEvent>> listenerEntry : listeners.entrySet()) {
            if(listenerEntry.getKey().equals(base)) {
                listenerEntry.getValue().apply(event);
            }
        }
    }

    public void callEvent(EnchantmentEvent event) {
        for (Map.Entry<Object, EnchantmentListener<EnchantmentEvent>> listenerEntry : listeners.entrySet()) {
            event.setEnchantment((Enchantment) listenerEntry.getKey());
            if (listenerEntry.getKey() instanceof EnchantmentBase) {
                for (Enchantment enchantment : event.getItem().getEnchantments().keySet()) {
                    if (enchantment.getKey().equals(((EnchantmentBase) listenerEntry.getKey()).getKey()))
                        listenerEntry.getValue().apply(event);
                }
            } else {
                listenerEntry.getValue().apply(event);
            }
        }
    }
}
