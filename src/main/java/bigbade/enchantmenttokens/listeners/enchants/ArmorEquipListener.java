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

import bigbade.enchantmenttokens.api.EnchantmentBase;
import bigbade.enchantmenttokens.events.EnchantmentEvent;
import com.codingforcookies.armorequip.ArmorEquipEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ArmorEquipListener extends BasicEnchantListener<ArmorEquipEvent> implements Listener {
    private Map<EnchantmentBase, EnchantmentListener<EnchantmentEvent<ArmorEquipEvent>>> oldArmorListeners;
    private Map<EnchantmentBase, EnchantmentListener<EnchantmentEvent<ArmorEquipEvent>>> newArmorListeners;

    public ArmorEquipListener(Map<EnchantmentBase, EnchantmentListener<EnchantmentEvent<ArmorEquipEvent>>> oldArmorListeners, Map<EnchantmentBase, EnchantmentListener<EnchantmentEvent<ArmorEquipEvent>>> newItemListeners) {
        super(null);
        this.oldArmorListeners = oldArmorListeners;
        this.newArmorListeners = newItemListeners;
    }

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent event) {
        ItemStack item = event.getOldArmorPiece();
        if(item != null) {
            EnchantmentEvent<ArmorEquipEvent> enchantmentEvent = new EnchantmentEvent<>(event).setItem(item).setUser(event.getPlayer());
            callListeners(item, enchantmentEvent, oldArmorListeners);
        }
        item = event.getNewArmorPiece();
        if(item != null) {
            EnchantmentEvent<ArmorEquipEvent> enchantmentEvent = new EnchantmentEvent<>(event).setItem(item).setUser(event.getPlayer());
            callListeners(item, enchantmentEvent, newArmorListeners);
        }
    }
}