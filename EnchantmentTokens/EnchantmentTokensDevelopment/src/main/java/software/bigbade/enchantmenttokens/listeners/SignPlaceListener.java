package software.bigbade.enchantmenttokens.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import software.bigbade.enchantmenttokens.api.CustomEnchantment;
import software.bigbade.enchantmenttokens.localization.TranslatedMessage;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;

public class SignPlaceListener implements Listener {
    private EnchantmentHandler handler;

    public SignPlaceListener(EnchantmentHandler handler) {
        this.handler = handler;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (("[" + TranslatedMessage.translate("enchantment") + "]").equalsIgnoreCase(event.getLine(0)) && event.getLine(1) != null) {
            String name = event.getLine(1);
            assert name != null;
            for (CustomEnchantment base : handler.getAllEnchants()) {
                if (name.equalsIgnoreCase(base.getName())) {
                    updateSign(base, event);
                    return;
                }
            }
            event.getPlayer().sendMessage(TranslatedMessage.translate("enchantment.add.fail"));
        }
    }

    private void updateSign(CustomEnchantment base, SignChangeEvent event) {
        event.getPlayer().sendMessage(TranslatedMessage.translate("enchantment.add", base.getName()));
        event.setLine(0, "[" + TranslatedMessage.translate("enchantment") + "]");
        event.setLine(1, base.getName());
    }
}