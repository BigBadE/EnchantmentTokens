/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners.enchants;

import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.api.EventFactory;
import software.bigbade.enchantmenttokens.api.ListenerType;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.utils.SignHandler;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

public class BlockBreakListener extends BasicEnchantListener implements Listener {
    private final SignHandler handler;

    public BlockBreakListener(ListenerManager<?> enchantListeners, SignHandler handler) {
        super(enchantListeners);
        this.handler = handler;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        EnchantmentEvent<BlockBreakEvent> enchantmentEvent = new EventFactory<BlockBreakEvent>().createEvent(event, ListenerType.BLOCK_BREAK, item, event.getPlayer()).setTargetBlock(event.getBlock());
        callListeners(enchantmentEvent);

        if (event.getBlock().getState() instanceof Sign) {
            Sign sign = (Sign) event.getBlock().getState();
            if (sign.getLine(0).equals("[Enchantment]")) {
                handler.removeSign(sign.getLocation());
            }
        }
    }
}