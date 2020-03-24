package software.bigbade.enchantmenttokens.listeners.enchants;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

public class BasicEnchantListener {
    private ListenerManager listeners;

    public BasicEnchantListener() {}

    public BasicEnchantListener(ListenerManager listeners) {
        this.listeners = listeners;
    }

    public void callListeners(EnchantmentEvent event) {
        listeners.callEvent(event);
        Bukkit.getPluginManager().callEvent(event);
    }

    public void callListeners(EnchantmentEvent event, ListenerManager listeners) {
        listeners.callEvent(event);
        Bukkit.getPluginManager().callEvent(event);
    }

    public void callForAllItems(Player player, ListenerManager manager, EnchantmentEvent event) {
        for (ItemStack item : player.getInventory().getArmorContents()) {
            event.setItem(item);
            callListeners(event, manager);
        }
        event.setItem(player.getInventory().getItemInMainHand());
        callListeners(event, manager);
        event.setItem(player.getInventory().getItemInMainHand());
        callListeners(event, manager);
    }

    public void callForAllItems(Player player, EnchantmentEvent event) {
        callForAllItems(player, listeners, event);
    }
}