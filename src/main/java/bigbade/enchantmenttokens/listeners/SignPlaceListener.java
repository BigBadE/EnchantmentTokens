package bigbade.enchantmenttokens.listeners;

import bigbade.enchantmenttokens.EnchantmentTokens;
import bigbade.enchantmenttokens.api.EnchantmentBase;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignPlaceListener implements Listener {
    private EnchantmentTokens main;

    public SignPlaceListener(EnchantmentTokens main) {
        this.main = main;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[enchantment]")) {
            for (EnchantmentBase base : main.enchantments) {
                if (base.getName().equalsIgnoreCase(event.getLine(1))) {
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Added enchantment " + base.getName());
                    event.setLine(0, "[Enchantment]");
                    event.setLine(1, base.getName());
                    return;
                }
            }
            for (Enchantment base : main.vanillaEnchants) {
                if (base.getName().equalsIgnoreCase(event.getLine(1))) {
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Added enchantment " + base.getName());
                    event.setLine(0, "[Enchantment]");
                    event.setLine(1, base.getName());
                    return;
                }
            }
            event.getPlayer().sendMessage(ChatColor.RED + "Could not find that enchantment!");
        }
    }
}