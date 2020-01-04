package bigbade.enchantmenttokens.listeners;

import bigbade.enchantmenttokens.EnchantmentTokens;
import bigbade.enchantmenttokens.api.EnchantUtils;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class SignClickListener implements Listener {
    private EnchantmentTokens main;
    private ConfigurationSection section;

    public SignClickListener(EnchantmentTokens main) {
        this.main = main;
        section = main.getConfig().getConfigurationSection("enchant");
    }

    @EventHandler
    public void onSignInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getHand() == EquipmentSlot.HAND)
            if (event.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                if (sign.getLine(0).equals("[Enchantment]")) {
                    ItemStack itemStack = event.getItem();
                    EnchantUtils.addEnchantment(itemStack, sign.getLine(1), main, event.getPlayer(), section, true);
                }
            }
    }
}
