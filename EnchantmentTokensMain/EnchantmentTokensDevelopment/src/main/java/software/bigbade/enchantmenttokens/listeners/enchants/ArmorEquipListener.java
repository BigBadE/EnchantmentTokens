/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners.enchants;

import com.codingforcookies.armorequip.ArmorEquipEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.api.EventFactory;
import software.bigbade.enchantmenttokens.api.ListenerType;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

public class ArmorEquipListener extends BasicEnchantListener implements Listener {
    private final ListenerManager oldArmorListeners;
    private final ListenerManager newArmorListeners;

    public ArmorEquipListener(ListenerManager oldArmorListeners, ListenerManager newArmorListeners) {
        super(null);
        this.oldArmorListeners = oldArmorListeners;
        this.newArmorListeners = newArmorListeners;
    }

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent event) {
        ItemStack item = event.getOldArmorPiece();
        if(item != null) {
            EnchantmentEvent enchantmentEvent = EventFactory.createEvent(ListenerType.UNEQUIP, item).setUser(event.getPlayer());
            callListeners(enchantmentEvent, oldArmorListeners);
        }
        item = event.getNewArmorPiece();
        if(item != null) {
            EnchantmentEvent enchantmentEvent = EventFactory.createEvent(ListenerType.EQUIP, item).setUser(event.getPlayer());
            callListeners(enchantmentEvent, newArmorListeners);
        }
    }
}