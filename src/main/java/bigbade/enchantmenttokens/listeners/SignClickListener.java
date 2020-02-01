package bigbade.enchantmenttokens.listeners;

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

import bigbade.enchantmenttokens.api.EnchantUtils;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class SignClickListener implements Listener {
    private EnchantUtils utils;

    public SignClickListener(EnchantUtils utils) {
        this.utils = utils;
    }

    @EventHandler
    public void onSignInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getHand() == EquipmentSlot.HAND) {
            assert event.getClickedBlock() != null;
            if (event.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                if (sign.getLine(0).equals("[Enchantment]")) {
                    ItemStack itemStack = event.getItem();
                    utils.addEnchantment(itemStack, sign.getLine(1), event.getPlayer(), false);
                    event.getPlayer().sendSignChange(event.getClickedBlock().getLocation(), new String[]{"[Enchantment]", sign.getLine(1), "", ""});
                }
            }
        }
    }
}
