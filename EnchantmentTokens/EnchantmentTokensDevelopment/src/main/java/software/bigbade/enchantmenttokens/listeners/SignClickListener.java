/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners;

import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantUtils;

public class SignClickListener implements Listener {
    private final EnchantUtils utils;

    public SignClickListener(EnchantUtils utils) {
        this.utils = utils;
    }

    @EventHandler
    public void onSignInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getHand() == EquipmentSlot.HAND && event.getClickedBlock() != null && event.getClickedBlock().getState() instanceof Sign) {
            Sign sign = (Sign) event.getClickedBlock().getState();
            if (sign.getLine(0).equals("[Enchantment]")) {
                ItemStack itemStack = event.getItem();
                utils.addEnchantment(itemStack, sign.getLine(1), event.getPlayer());
                event.getPlayer().sendSignChange(event.getClickedBlock().getLocation(), new String[]{"[Enchantment]", sign.getLine(1), "", ""});
            }
        }
    }
}
