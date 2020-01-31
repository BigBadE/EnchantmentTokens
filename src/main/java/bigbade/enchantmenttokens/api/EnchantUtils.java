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
    public static void addEnchantment(ItemStack itemStack, String name, EnchantmentTokens main, Player player, boolean key) {
        for (EnchantmentBase base : main.getEnchantments()) {
            if (base.getName().equals(name))
                if (base.canEnchantItem(itemStack)) {
                    long price;
                    int level = base.getStartLevel();
                    for (Map.Entry<Enchantment, Integer> enchants : itemStack.getEnchantments().entrySet()) {
                        if (enchants.getKey().getKey().equals(base.getKey())) {
                            level = enchants.getValue();
                            break;
                        }
                    }
                    if (level > base.getMaxLevel()) {
                        player.sendMessage(TranslatedMessage.translate("enchantment.max.message"));
                        return;
                    }
                    price = base.getDefaultPrice(level);
                    EnchantmentPlayer player1 = main.getPlayerHandler().getPlayer(player);
                    String priceString;
                    if (player1.usingGems())
                        priceString = TranslatedMessage.translate("enchantment.max") + " " + base.getDefaultPrice(level) + "G";
                    else
                        priceString = TranslatedMessage.translate("dollar.symbol") + TranslatedMessage.translate("enchantment.max") + " " + base.getDefaultPrice(level);
                    if (player1.getGems() >= price) {
                        if (key)
                            player1.addGems(-price);
                        player.sendMessage(TranslatedMessage.translate("enchantment.bought.success", base.getName(), "" + level));
                        ItemMeta meta = itemStack.getItemMeta();
                        assert meta != null;
                        meta.addEnchant(base, level + 1, true);
                        List<String> lore;
                        lore = meta.getLore();
                        if (lore == null)
                            lore = new ArrayList<>();
                        String temp = null;
                        String priceMessage = TranslatedMessage.translate("enchantment.price");
                        if (!key) {
                            if (lore.get(lore.size() - 1).startsWith(priceMessage)) {
                                temp = lore.get(lore.size() - 1);
                                lore.remove(temp);
                            }
                        }
                        if (level != 0)
                            lore.remove(ChatColor.GRAY + base.getName() + " " + EnchantmentGUIListener.getRomanNumeral(level - 1));
                        lore.add(ChatColor.GRAY + base.getName() + " " + EnchantmentGUIListener.getRomanNumeral(level));
                        if (temp != null) {
                            int currentPrice = Integer.parseInt(temp.substring(9, temp.length() - 1));
                            if (player1.usingGems())
                                temp = priceMessage + (currentPrice + base.getDefaultPrice(level)) + "G";
                            else
                                temp = priceMessage + TranslatedMessage.translate("dollar.symbol") + (currentPrice + base.getDefaultPrice(level));
                            lore.add(temp);
                        }
                        meta.setLore(lore);
                        itemStack.setItemMeta(meta);
                        for (Location location : main.signHandler.getSigns())
                            if (level >= base.getMaxLevel())
                                player.sendSignChange(location, new String[]{"[" + TranslatedMessage.translate("enchantment") + "]", base.getName(), "Price: Maxed!", ""});
                            else {
                                player.sendSignChange(location, new String[]{"[" + TranslatedMessage.translate("enchantment") + "]", base.getName(), priceString, ""});
                            }
                        main.getListenerHandler().onEnchant(itemStack, base, player);
                    } else

                        player.sendMessage(TranslatedMessage.translate("enchantment.bought.fail", priceString));

                    return;
                }
        }
        for (VanillaEnchant base : main.getVanillaEnchantments()) {
            boolean correct = false;
            if (base.getName().equals(name))
                correct = true;
            if (correct)
                if (base.canEnchantItem(itemStack)) {
                    long price;
                    int level = 1;
                    for (Map.Entry<Enchantment, Integer> enchants : itemStack.getEnchantments().entrySet())
                        if (enchants.getKey().getKey().equals(base.getKey())) {
                            level = enchants.getValue() + 1;
                            break;
                        }
                    if (level > base.getMaxLevel()) {
                        player.sendMessage(TranslatedMessage.translate("enchantment.bought.max"));
                        return;
                    }
                    price = base.getDefaultPrice(level);
                    EnchantmentPlayer player1 = main.getPlayerHandler().getPlayer(player);
                    if (player1.getGems() >= price) {
                        player1.addGems(-price);
                        player.sendMessage(TranslatedMessage.translate("enchantment.bought.success", base.getName(), "" + level));
                        itemStack.addEnchantment(base, level);
                        ItemMeta meta = itemStack.getItemMeta();
                        assert meta != null;
                        List<String> lore = meta.getLore();
                        if (lore == null)
                            lore = new ArrayList<>();
                        lore.add(base.getName() + ": " + EnchantmentGUIListener.getRomanNumeral(level));
                        meta.setLore(lore);
                        itemStack.setItemMeta(meta);
                        for (Location location : main.signHandler.getSigns())
                            player.sendSignChange(location, new String[]{"[" + TranslatedMessage.translate("enchantment") + "]", base.getName(), "", ""});
                    } else {
                        String priceString;
                        if(player1.usingGems())
                            priceString = price + "G";
                        else
                            priceString = TranslatedMessage.translate("dollar.symbol") + price;
                        player.sendMessage(TranslatedMessage.translate("enchantment.bought.fail", priceString));
                    }
                    return;
                }
        }
        player.sendMessage(TranslatedMessage.translate("enchantment.add.fail"));
    }
}
