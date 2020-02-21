package software.bigbade.enchantmenttokens.listeners;

/*
EnchantmentTokens
Copyright (C) 2019-2020 Big_Bad_E
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.localization.TranslatedMessage;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignPlaceListener implements Listener {
    private EnchantmentHandler handler;

    public SignPlaceListener(EnchantmentHandler handler) {
        this.handler = handler;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if ("[enchantment]".equalsIgnoreCase(event.getLine(0))) {
            for (EnchantmentBase base : handler.getAllEnchants()) {
                if(updateSign(base, event)) return;
            }
            event.getPlayer().sendMessage(TranslatedMessage.translate( "enchantment.add.fail"));
        }
    }

    private boolean updateSign(EnchantmentBase base, SignChangeEvent event) {
        System.out.println(base.getName() + " tested against " + event.getLine(1));
        if (base.getName().equalsIgnoreCase(event.getLine(1))) {
            event.getPlayer().sendMessage(TranslatedMessage.translate("enchantment.add", base.getName()));
            event.setLine(0, "[" + TranslatedMessage.translate( "enchantment") + "]");
            event.setLine(1, base.getName());
            return true;
        }
        return false;
    }
}