/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners.enchants;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.api.EventFactory;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

public class BlockDamageListener extends BasicEnchantListener<BlockDamageEvent> implements Listener {

    public BlockDamageListener(ListenerManager<BlockDamageEvent> eventListeners) {
        super(eventListeners);
    }

    @EventHandler
    public void blockBreakStart(BlockDamageEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(item.getType() != Material.AIR) {
            EnchantmentEvent<BlockDamageEvent> enchantmentEvent = EventFactory.createCancellableEvent(event, item, event.getPlayer()).setTargetBlock(event.getBlock());
            callListeners(enchantmentEvent);
        }
    }
}
