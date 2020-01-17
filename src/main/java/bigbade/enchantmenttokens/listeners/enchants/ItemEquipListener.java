package bigbade.enchantmenttokens.listeners.enchants;

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

import bigbade.enchantmenttokens.EnchantmentTokens;
import bigbade.enchantmenttokens.api.EnchantmentBase;
import com.codingforcookies.armorequip.ArmorEquipEvent;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Level;

public class ItemEquipListener extends BasicEnchantListener implements Listener {
    private Map<EnchantmentBase, Method> oldItemListeners;
    private Map<EnchantmentBase, Method> newItemListeners;

    public ItemEquipListener(Map<EnchantmentBase, Method> oldItemListeners, Map<EnchantmentBase, Method> newItemListeners) {
        super(null);
        this.oldItemListeners = oldItemListeners;
        this.newItemListeners = newItemListeners;
    }

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent event) {
        ItemStack item = event.getOldArmorPiece();
        if(item != null) {
            for (Map.Entry<EnchantmentBase, Method> enchantment : oldItemListeners.entrySet()) {
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
        item = event.getNewArmorPiece();
        if(item != null) {
            for (Map.Entry<EnchantmentBase, Method> enchantment : newItemListeners.entrySet()) {
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

}