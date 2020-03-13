package software.bigbade.enchantmenttokens.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.localization.TranslatedTextMessage;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantUtils;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;

public class SignPlaceListener implements Listener {
    private EnchantmentHandler handler;

    public SignPlaceListener(EnchantmentHandler handler) {
        this.handler = handler;
    }

    private static final String ENCHANTMENT = new TranslatedTextMessage("enchantment").getText();

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (("[" + ENCHANTMENT + "]").equalsIgnoreCase(event.getLine(0)) && event.getLine(1) != null) {
            String name = event.getLine(1);
            assert name != null;
            for (EnchantmentBase base : handler.getAllEnchants()) {
                if (name.equalsIgnoreCase(base.getName())) {
                    updateSign(base, event);
                    return;
                }
            }
            event.getPlayer().sendMessage(EnchantUtils.ENCHANTMENTFAIL);
        }
    }

    private static final TranslatedTextMessage ADDENCHANTMENT = new TranslatedTextMessage("enchantment.add");

    private void updateSign(EnchantmentBase base, SignChangeEvent event) {
        event.getPlayer().sendMessage(ADDENCHANTMENT.getText(base.getName()));
        event.setLine(0, "[" + ENCHANTMENT + "]");
        event.setLine(1, base.getName());
    }
}