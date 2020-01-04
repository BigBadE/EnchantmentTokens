package bigbade.enchantmenttokens.listeners.enchants;

import bigbade.enchantmenttokens.EnchantmentTokens;
import bigbade.enchantmenttokens.api.EnchantmentBase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.function.Consumer;

public class ItemSwitchListener implements Listener {
    private Map<EnchantmentBase, Consumer<Event>> oldItemListeners;
    private Map<EnchantmentBase, Consumer<Event>> newItemListeners;
    private EnchantmentTokens main;

    public ItemSwitchListener(Map<EnchantmentBase, Consumer<Event>> oldItemListeners, Map<EnchantmentBase, Consumer<Event>> newItemListeners, EnchantmentTokens main) {
        this.oldItemListeners = oldItemListeners;
        this.newItemListeners = newItemListeners;
        this.main = main;
    }

    @EventHandler
    public void onItemSwitch(PlayerItemHeldEvent event) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
        for (Location location : main.signs) {
            if (location.getChunk().isLoaded() && location.getWorld() == event.getPlayer().getWorld()) {
                    try {
                        event.getPlayer().sendSignChange(location, new String[]{"[Enchantment]", ((Sign) location.getBlock().getState()).getLine(1), "", ""});
                    } catch (Exception e) {
                        main.signs.remove(location);
                    }
                }
            }
        });
        ItemStack old = event.getPlayer().getInventory().getItem(event.getPreviousSlot());
        if(old != null) {
            for (Map.Entry<EnchantmentBase, Consumer<Event>> enchantment : oldItemListeners.entrySet()) {
                for (Enchantment enchantment1 : old.getEnchantments().keySet()) {
                    if (enchantment1.getKey().getNamespace().equals("enchantmenttokens") && enchantment1.equals(enchantment.getKey())) {
                        enchantment.getValue().accept(event);
                    }
                }
            }
        }
        ItemStack newItem = event.getPlayer().getInventory().getItem(event.getPreviousSlot());
        if(newItem != null) {
            for (Map.Entry<EnchantmentBase, Consumer<Event>> enchantment : newItemListeners.entrySet()) {
                for (Enchantment enchantment1 : newItem.getEnchantments().keySet()) {
                    if (enchantment1.getKey().getNamespace().equals("enchantmenttokens") && enchantment1.equals(enchantment.getKey())) {
                        enchantment.getValue().accept(event);
                    }
                }
            }
        }
    }
}