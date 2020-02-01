package bigbade.enchantmenttokens.api;

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

import bigbade.enchantmenttokens.EnchantmentTokens;
import bigbade.enchantmenttokens.listeners.gui.EnchantmentGUIListener;
import bigbade.enchantmenttokens.localization.TranslatedMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnchantUtils {
    public static void addEnchantment(ItemStack itemStack, String name, EnchantmentTokens main, Player player, boolean simulate) {
        for (EnchantmentBase base : main.getEnchantments()) {
            if (base.getName().equals(name))
                if (base.canEnchantItem(itemStack)) {
                    addEnchantment(itemStack, base, player, main, simulate);
                }
        }
        for (VanillaEnchant base : main.getVanillaEnchantments()) {
            if (base.getName().equals(name))
                if (base.canEnchantItem(itemStack)) {
                    addEnchantment(itemStack, base, player, main, simulate);
                }
        }
        player.sendMessage(TranslatedMessage.translate("enchantment.add.fail"));
    }

    private static void addEnchantment(ItemStack item, EnchantmentBase base, Player player, EnchantmentTokens main, boolean simulate) {
        int level = getLevel(item, base);
        if (level > base.getMaxLevel()) {
            player.sendMessage(TranslatedMessage.translate("enchantment.max.message"));
            return;
        }
        long price = base.getDefaultPrice(level);
        EnchantmentPlayer enchantmentPlayer = main.getPlayerHandler().getPlayer(player);
        if (enchantmentPlayer.getGems() >= price) {
            if (!simulate)
                enchantmentPlayer.addGems(-price);
            player.sendMessage(TranslatedMessage.translate("enchantment.bought.success", base.getName(), "" + level));
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            meta.addEnchant(base, level + 1, true);
            List<String> lore;
            lore = meta.getLore();
            if (lore == null)
                lore = new ArrayList<>();
            String temp = null;
            String priceMessage = TranslatedMessage.translate("enchantment.price");
            if (lore.size() > 0)
                if (lore.get(lore.size() - 1).startsWith(priceMessage)) {
                    temp = lore.get(lore.size() - 1);
                    lore.remove(temp);
                }
            if (level != 0)
                lore.remove(ChatColor.GRAY + base.getName() + " " + EnchantmentGUIListener.getRomanNumeral(level - 1));
            lore.add(ChatColor.GRAY + base.getName() + " " + EnchantmentGUIListener.getRomanNumeral(level));
            if (temp != null) {
                int currentPrice = Integer.parseInt(temp.substring(9, temp.length() - 1));
                if (enchantmentPlayer.usingGems())
                    temp = priceMessage + (currentPrice + base.getDefaultPrice(level)) + "G";
                else
                    temp = priceMessage + TranslatedMessage.translate("dollar.symbol", "" + (currentPrice + base.getDefaultPrice(level)));
                lore.add(temp);
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
            updateSigns(level, base, main.getSigns(), enchantmentPlayer);
            main.getListenerHandler().onEnchant(item, base, player);
        } else
            player.sendMessage(TranslatedMessage.translate("enchantment.bought.fail", getPriceString(enchantmentPlayer, level, base)));
    }

    private static int getLevel(ItemStack item, EnchantmentBase enchantment) {
        for (Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
            if (entry.getKey().getKey().equals(enchantment.getKey())) {
                return entry.getValue();
            }
        }
        return enchantment.getStartLevel();
    }

    private static String getPriceString(EnchantmentPlayer player, int level, EnchantmentBase base) {
        if (player.usingGems())
            return base.getDefaultPrice(level) + "G";
        else
            return TranslatedMessage.translate("dollar.symbol", "" + base.getDefaultPrice(level));
    }

    private static void updateSigns(int level, EnchantmentBase base, List<Location> signs, EnchantmentPlayer player) {
        for (Location location : signs)
            if (level >= base.getMaxLevel())
                player.getPlayer().sendSignChange(location, new String[]{"[" + TranslatedMessage.translate("enchantment") + "]", base.getName(), TranslatedMessage.translate("enchantment.price.maxed"), ""});
            else {
                player.getPlayer().sendSignChange(location, new String[]{"[" + TranslatedMessage.translate("enchantment") + "]", base.getName(), getPriceString(player, level, base), ""});
            }
    }
}
