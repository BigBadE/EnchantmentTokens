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
import bigbade.enchantmenttokens.api.EnchantmentBase;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.function.Consumer;

public class BlockBreakListener implements Listener {
    private Map<EnchantmentBase, Consumer<Event>> enchantListeners;
    private EnchantmentTokens main;

    public BlockBreakListener(Map<EnchantmentBase, Consumer<Event>> enchantListeners, EnchantmentTokens main) {
        this.enchantListeners = enchantListeners;
        this.main = main;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        for (Map.Entry<EnchantmentBase, Consumer<Event>> enchantment : enchantListeners.entrySet()) {
            for(Enchantment enchantment1 : item.getEnchantments().keySet()) {
                if (enchantment1.getKey().getNamespace().equals("enchantmenttokens") && enchantment1.equals(enchantment.getKey())) {
                    enchantment.getValue().accept(event);
                }
            }
        }

        if(event.getBlock().getState() instanceof Sign) {
            Sign sign = (Sign) event.getBlock().getState();
            if(sign.getLine(0).equals("[Enchantment]")) {
                main.signs.remove(sign.getLocation());
            }
        }
    }
}