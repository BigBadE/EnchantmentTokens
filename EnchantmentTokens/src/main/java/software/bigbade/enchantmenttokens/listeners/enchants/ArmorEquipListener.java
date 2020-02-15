package software.bigbade.enchantmenttokens.listeners.enchants;

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

import software.bigbade.enchantmenttokens.api.ListenerType;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;
import com.codingforcookies.armorequip.ArmorEquipEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class ArmorEquipListener extends BasicEnchantListener implements Listener {
    private ListenerManager oldArmorListeners;
    private ListenerManager newArmorListeners;

    public ArmorEquipListener(ListenerManager oldArmorListeners, ListenerManager newArmorListeners) {
        super(null);
        this.oldArmorListeners = oldArmorListeners;
        this.newArmorListeners = newArmorListeners;
    }

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent event) {
        ItemStack item = event.getOldArmorPiece();
        if(item != null) {
            EnchantmentEvent enchantmentEvent = new EnchantmentEvent(ListenerType.UNEQUIP, item).setUser(event.getPlayer());
            callListeners(enchantmentEvent, oldArmorListeners);
        }
        item = event.getNewArmorPiece();
        if(item != null) {
            EnchantmentEvent enchantmentEvent = new EnchantmentEvent(ListenerType.EQUIP, item).setUser(event.getPlayer());
            callListeners(enchantmentEvent, newArmorListeners);
        }
    }
}