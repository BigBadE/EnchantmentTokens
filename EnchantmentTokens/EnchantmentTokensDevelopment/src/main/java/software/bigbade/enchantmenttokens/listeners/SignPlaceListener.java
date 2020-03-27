package software.bigbade.enchantmenttokens.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;

public class SignPlaceListener implements Listener {
    private EnchantmentHandler handler;

    public SignPlaceListener(EnchantmentHandler handler) {
        this.handler = handler;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (("[" + StringUtils.ENCHANTMENT + "]").equalsIgnoreCase(event.getLine(0)) && event.getLine(1) != null) {
            String name = event.getLine(1);
            assert name != null;
            for (EnchantmentBase base : handler.getAllEnchants()) {
                if (name.equalsIgnoreCase(base.getEnchantName())) {
                    updateSign(base, event);
                    return;
                }
            }
            event.getPlayer().sendMessage(StringUtils.ENCHANTMENT_ADD_FAIL);
        }
    }

    private void updateSign(EnchantmentBase base, SignChangeEvent event) {
        event.getPlayer().sendMessage(StringUtils.ENCHANTMENT_ADD.translate(base.getEnchantName()));
        event.setLine(0, "[" + StringUtils.ENCHANTMENT + "]");
        event.setLine(1, base.getEnchantName());
    }
}