package bigbade.enchantmenttokens.listeners.enchants;

import bigbade.enchantmenttokens.EnchantmentTokens;
import bigbade.enchantmenttokens.api.EnchantmentBase;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

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

public class BasicEnchantListener {
    private Map<EnchantmentBase, Method> listeners = new HashMap<>();

    public BasicEnchantListener(Map<EnchantmentBase, Method> listeners) {
        this.listeners = listeners;
    }

    public void callListeners(ItemStack item, Event event) {
        for (Map.Entry<EnchantmentBase, Method> enchantment : listeners.entrySet()) {
            if (item.containsEnchantment(enchantment.getKey())) {
                try {
                    enchantment.getValue().invoke(enchantment.getKey(), event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    if (e instanceof IllegalAccessException) {
                        EnchantmentTokens.LOGGER.log(Level.SEVERE, "Did not have permission " + enchantment.getValue().getName() + " for enchantment " + enchantment.getKey().name + ", make sure it isn't private/protected", e
                        );
                    } else {
                        EnchantmentTokens.LOGGER.log(Level.SEVERE, "Could not invoke " + enchantment.getValue().getName() + ", check arguments.", e);
                    }
                }
            }
        }
    }
}
