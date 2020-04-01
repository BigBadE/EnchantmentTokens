/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.localization.TranslatedStringMessage;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import software.bigbade.enchantmenttokens.utils.players.PlayerHandler;

import java.util.Locale;

@RequiredArgsConstructor
public class SignPlaceListener implements Listener {
    private final EnchantmentHandler handler;
    private final PlayerHandler playerHandler;

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Locale locale = playerHandler.getPlayer(event.getPlayer()).getLanguage();
        if (("[" + new TranslatedStringMessage(locale, StringUtils.ENCHANTMENT).translate() + "]").equalsIgnoreCase(event.getLine(0)) && event.getLine(1) != null) {
            String name = event.getLine(1);
            assert name != null;
            for (EnchantmentBase base : handler.getAllEnchants()) {
                if (name.equalsIgnoreCase(base.getEnchantmentName())) {
                    updateSign(base, event, locale);
                    return;
                }
            }
            event.getPlayer().sendMessage(new TranslatedStringMessage(locale, StringUtils.ENCHANTMENT_ADD_FAIL).translate());
        }
    }

    private void updateSign(EnchantmentBase base, SignChangeEvent event, Locale locale) {

        event.getPlayer().sendMessage(new TranslatedStringMessage(locale, StringUtils.ENCHANTMENT_ADD).translate(base.getEnchantmentName()));
        event.setLine(0, "[" + StringUtils.ENCHANTMENT + "]");
        event.setLine(1, base.getEnchantmentName());
    }
}