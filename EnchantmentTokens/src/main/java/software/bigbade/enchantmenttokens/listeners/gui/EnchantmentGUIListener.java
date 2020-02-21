package software.bigbade.enchantmenttokens.listeners.gui;

import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.gui.EnchantmentGUI;
import software.bigbade.enchantmenttokens.utils.EnchantButton;
import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class EnchantmentGUIListener implements Listener {
    private static String[] c = {"", "C", "CC", "CCC", "CD", "D",
            "DC", "DCC", "DCCC", "CM"};
    private static String[] x = {"", "X", "XX", "XXX", "XL", "L",
            "LX", "LXX", "LXXX", "XC"};
    private static String[] i = {"", "I", "II", "III", "IV", "V",
            "VI", "VII", "VIII", "IX"};

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
        EnchantButton button = player.getCurrentGUI().getButton(event.getSlot());
        if(button == null) return;
        EnchantmentGUI inventory = button.click(event.getInventory().getItem(4));
        if (inventory == null) {
            player.setCurrentGUI(null);
            player.getPlayer().closeInventory();
        } else {
            player.setCurrentGUI(null);
            player.getPlayer().openInventory(inventory.getInventory());
            player.setCurrentGUI(inventory);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        EnchantmentPlayer enchantPlayer = handler.getPlayer((Player) event.getPlayer());
        if (enchantPlayer.getCurrentGUI() != null && enchantPlayer.getCurrentGUI().getInventory() != null && enchantPlayer.getCurrentGUI().getInventory().equals(event.getInventory())) {
            scheduler.runTaskLater(() -> {
                EnchantmentGUI gui = enchantPlayer.getCurrentGUI();
                enchantPlayer.setCurrentGUI(null);
                event.getPlayer().openInventory(event.getInventory());
                enchantPlayer.setCurrentGUI(gui);
            }, 1L);
        }
    }

    public static String getRomanNumeral(int level) {
        return c[level / 100] +
                x[(level % 100) / 10] +
                i[level % 10];
    }
}
