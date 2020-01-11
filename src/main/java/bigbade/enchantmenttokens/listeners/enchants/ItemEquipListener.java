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
import bigbade.enchantmenttokens.api.ArmorType;
import bigbade.enchantmenttokens.api.EnchantmentBase;
import com.codingforcookies.armorequip.ArmorEquipEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.function.Consumer;

public class ItemEquipListener implements Listener {
    private Map<EnchantmentBase, Consumer<Event>> oldItemListeners;
    private Map<EnchantmentBase, Consumer<Event>> newItemListeners;
    private EnchantmentTokens main;

    public ItemEquipListener(Map<EnchantmentBase, Consumer<Event>> oldItemListeners, Map<EnchantmentBase, Consumer<Event>> newItemListeners, EnchantmentTokens main) {
        this.oldItemListeners = oldItemListeners;
        this.newItemListeners = newItemListeners;
        this.main = main;
    }

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent event) {
        ItemStack item = event.getOldArmorPiece();
        if(item != null) {
            for (Map.Entry<EnchantmentBase, Consumer<Event>> enchantment : oldItemListeners.entrySet()) {
                for (Enchantment enchantment1 : item.getEnchantments().keySet()) {
                    if (enchantment1.getKey().getNamespace().equals("enchantmenttokens") && enchantment1.equals(enchantment.getKey())) {
                        enchantment.getValue().accept(event);
                    }
                }
            }
        }
        item = event.getNewArmorPiece();
        if(item != null) {
            for (Map.Entry<EnchantmentBase, Consumer<Event>> enchantment : newItemListeners.entrySet()) {
                for (Enchantment enchantment1 : item.getEnchantments().keySet()) {
                    if (enchantment1.getKey().getNamespace().equals("enchantmenttokens") && enchantment1.equals(enchantment.getKey())) {
                        enchantment.getValue().accept(event);
                    }
                }
            }
        }

    }

}