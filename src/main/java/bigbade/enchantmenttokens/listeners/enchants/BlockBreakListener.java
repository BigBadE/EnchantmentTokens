package bigbade.enchantmenttokens.listeners.enchants;

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