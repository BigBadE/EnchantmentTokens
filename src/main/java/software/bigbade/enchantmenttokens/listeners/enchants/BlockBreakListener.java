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

import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.listeners.SignPacketHandler;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreakListener extends BasicEnchantListener<BlockBreakEvent> implements Listener {
    private SignPacketHandler handler;

    public BlockBreakListener(ListenerManager enchantListeners, SignPacketHandler handler) {
        super(enchantListeners);
        this.handler = handler;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        EnchantmentEvent<BlockBreakEvent> enchantmentEvent = new EnchantmentEvent<>(event, item).setTargetBlock(event.getBlock()).setUser(event.getPlayer());
        callListeners(enchantmentEvent);

        if(event.getBlock().getState() instanceof Sign) {
            Sign sign = (Sign) event.getBlock().getState();
            if(sign.getLine(0).equals("[Enchantment]")) {
                handler.removeSign(sign.getLocation());
            }
        }
    }
}