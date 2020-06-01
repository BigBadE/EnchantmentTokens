/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners.enchants;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.api.EventFactory;
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.localization.TranslatedStringMessage;
import software.bigbade.enchantmenttokens.utils.SignHandler;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

import java.util.Locale;

public class BlockBreakListener extends BasicEnchantListener<BlockBreakEvent> implements Listener {
    private final SignHandler handler;

    public BlockBreakListener(ListenerManager<BlockBreakEvent> enchantListeners, SignHandler handler) {
        super(enchantListeners);
        this.handler = handler;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        EnchantmentEvent<BlockBreakEvent> enchantmentEvent = EventFactory.createEvent(event, item, event.getPlayer()).setTargetBlock(event.getBlock());
        callListeners(enchantmentEvent);

        if (event.getBlock().getState() instanceof Sign) {
            Sign sign = (Sign) event.getBlock().getState();
            if (ChatColor.stripColor(sign.getLine(0)).equals("[" + StringUtils.ENCHANTMENT_LINE + "]")) {
                handler.removeSign(sign.getLocation());
            }
        }
    }
}