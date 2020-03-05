package software.bigbade.enchantmenttokens.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.localization.TranslatedMessage;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;

public class SignPlaceListener implements Listener {
    private EnchantmentHandler handler;

    public SignPlaceListener(EnchantmentHandler handler) {
        this.handler = handler;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (TranslatedMessage.translate("enchantment").equalsIgnoreCase(event.getLine(0))) {
            for (EnchantmentBase base : handler.getAllEnchants()) {
                if (updateSign(base, event)) return;
            }
            event.getPlayer().sendMessage(TranslatedMessage.translate("enchantment.add.fail"));
        }
    }

    private boolean updateSign(EnchantmentBase base, SignChangeEvent event) {
        if (base.getName().equalsIgnoreCase(event.getLine(1))) {
            event.getPlayer().sendMessage(TranslatedMessage.translate("enchantment.add", base.getName()));
            event.setLine(0, "[" + TranslatedMessage.translate("enchantment") + "]");
            event.setLine(1, base.getName());
            return true;
        }
        return false;
    }
}