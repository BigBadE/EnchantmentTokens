package bigbade.enchantmenttokens.listeners.enchants;

import bigbade.enchantmenttokens.EnchantmentTokens;
import bigbade.enchantmenttokens.api.EnchantmentBase;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.function.Consumer;

public class BlockDamageListener implements Listener {
    private Map<EnchantmentBase, Consumer<Event>> eventListeners;

    public BlockDamageListener(Map<EnchantmentBase, Consumer<Event>> eventListeners) {
        this.eventListeners = eventListeners;
    }

    @EventHandler
    public void blockBreakStart(BlockDamageEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(item.getType() != Material.AIR) {
            for (Map.Entry<EnchantmentBase, Consumer<Event>> enchantment : eventListeners.entrySet()) {
                for (Enchantment enchantment1 : item.getEnchantments().keySet()) {
                    if (enchantment1.getKey().getNamespace().equals("enchantmenttokens") && enchantment1.equals(enchantment.getKey())) {
                        enchantment.getValue().accept(event);
                    }
                }
            }
        }
    }
}
