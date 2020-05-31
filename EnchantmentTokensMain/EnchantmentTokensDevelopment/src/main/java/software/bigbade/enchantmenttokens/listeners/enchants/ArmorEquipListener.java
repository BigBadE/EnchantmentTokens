/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners.enchants;

import com.codingforcookies.armorequip.ArmorEquipEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.api.EventFactory;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

<<<<<<< HEAD:EnchantmentTokens/EnchantmentTokensDevelopment/src/main/java/software/bigbade/enchantmenttokens/listeners/enchants/ArmorEquipListener.java
public class ArmorEquipListener extends BasicEnchantListener<ArmorEquipEvent> implements Listener {
    private final ListenerManager<ArmorEquipEvent> oldArmorListeners;
    private final ListenerManager<ArmorEquipEvent> newArmorListeners;
=======
public class ArmorEquipListener extends BasicEnchantListener implements Listener {
    private final ListenerManager oldArmorListeners;
    private final ListenerManager newArmorListeners;
>>>>>>> 3d705af96ebb617ac55d44878c2077b5e14535b9:EnchantmentTokensMain/EnchantmentTokensDevelopment/src/main/java/software/bigbade/enchantmenttokens/listeners/enchants/ArmorEquipListener.java

    public ArmorEquipListener(ListenerManager<ArmorEquipEvent> oldArmorListeners, ListenerManager<ArmorEquipEvent> newArmorListeners) {
        super(null);
        this.oldArmorListeners = oldArmorListeners;
        this.newArmorListeners = newArmorListeners;
    }

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent event) {
        ItemStack item = event.getOldArmorPiece();
        if(item != null) {
            EnchantmentEvent<ArmorEquipEvent> enchantmentEvent = EventFactory.createEvent(event, item, event.getPlayer());
            callListeners(enchantmentEvent, oldArmorListeners);
        }
        item = event.getNewArmorPiece();
        if(item != null) {
            EnchantmentEvent<ArmorEquipEvent> enchantmentEvent = EventFactory.createEvent(event, item, event.getPlayer());
            callListeners(enchantmentEvent, newArmorListeners);
        }
    }
}