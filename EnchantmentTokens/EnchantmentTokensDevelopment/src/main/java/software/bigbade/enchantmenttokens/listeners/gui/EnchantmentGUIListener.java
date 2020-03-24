package software.bigbade.enchantmenttokens.listeners.gui;

import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.gui.CustomEnchantmentGUI;
import software.bigbade.enchantmenttokens.utils.CustomEnchantButton;
import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class EnchantmentGUIListener implements Listener {
    private EnchantmentPlayerHandler handler;
    private SchedulerHandler scheduler;

    public EnchantmentGUIListener(EnchantmentPlayerHandler handler, SchedulerHandler scheduler) {
        this.handler = handler;
        this.scheduler = scheduler;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        EnchantmentPlayer player = handler.getPlayer((Player) event.getWhoClicked());
        if (event.getClickedInventory() == null || event.getCurrentItem() == null || player.getCurrentGUI() == null)
            return;
        event.setCancelled(true);
        CustomEnchantButton button = player.getCurrentGUI().getButton(event.getSlot());
        if(button == null) return;
        CustomEnchantmentGUI inventory = button.click(player);
        player.setCurrentGUI(null);
        if (inventory == null) {
            player.getPlayer().closeInventory();
        } else {
            player.getPlayer().openInventory(inventory.getInventory());
            player.setCurrentGUI(inventory);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        EnchantmentPlayer enchantPlayer = handler.getPlayer((Player) event.getPlayer());
        if (enchantPlayer.getCurrentGUI() != null && enchantPlayer.getCurrentGUI().getInventory() != null && enchantPlayer.getCurrentGUI().getInventory().equals(event.getInventory())) {
            scheduler.runTaskLater(() -> {
                CustomEnchantmentGUI gui = enchantPlayer.getCurrentGUI();
                enchantPlayer.setCurrentGUI(null);
                event.getPlayer().openInventory(event.getInventory());
                enchantPlayer.setCurrentGUI(gui);
            }, 1L);
        }
    }
}