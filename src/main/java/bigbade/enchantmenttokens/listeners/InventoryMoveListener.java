package bigbade.enchantmenttokens.listeners;

import bigbade.enchantmenttokens.EnchantmentTokens;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryMoveListener implements Listener {
    private EnchantmentTokens main;

    public InventoryMoveListener(EnchantmentTokens main) {
        this.main = main;
    }

    @EventHandler
    public void onInventorySwap(InventoryClickEvent event) {
        if(event.getInventory().getHolder() == null) return;
        if(event.getInventory().getHolder().equals(event.getWhoClicked())) {
            if (event.getSlot() == event.getWhoClicked().getInventory().getHeldItemSlot()) {
                for (Location location : main.signs) {
                    if (location.getChunk().isLoaded() && location.getWorld() == event.getWhoClicked().getWorld()) {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(main, () ->
                                ((Player) event.getWhoClicked()).sendSignChange(location, new String[]{"[Enchantment]", ((Sign) location.getBlock().getState()).getLine(1), "", ""}));
                    }
                }
            }
        }
    }

}
