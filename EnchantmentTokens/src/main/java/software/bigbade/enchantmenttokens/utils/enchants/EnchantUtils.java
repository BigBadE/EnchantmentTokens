package software.bigbade.enchantmenttokens.utils.enchants;

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
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.api.VanillaEnchant;
import software.bigbade.enchantmenttokens.listeners.gui.EnchantmentGUIListener;
import software.bigbade.enchantmenttokens.localization.TranslatedMessage;
import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerHandler;
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
    private EnchantmentHandler handler;
    private EnchantmentPlayerHandler playerHandler;
    private ListenerHandler listenerHandler;
    private List<Location> signs;

    public EnchantUtils(EnchantmentHandler handler, EnchantmentPlayerHandler playerHandler, ListenerHandler listenerHandler, List<Location> signs) {
        this.handler = handler;
        this.playerHandler = playerHandler;
        this.listenerHandler = listenerHandler;
        this.signs = signs;
    }

    public void addEnchantment(ItemStack itemStack, String name, Player player, boolean simulate) {
        for (EnchantmentBase base : handler.getEnchantments()) {
            if (base.getName().equals(name))
                if (base.canEnchantItem(itemStack)) {
                    addEnchantmentBase(itemStack, base, player, simulate);
                    return;
                }
        }
        for (VanillaEnchant base : handler.getVanillaEnchants()) {
            if (base.getName().equals(name))
                if (base.canEnchantItem(itemStack)) {
                    addEnchantmentBase(itemStack, base, player, simulate);
                    return;
                }
        }
        player.sendMessage(TranslatedMessage.translate("enchantment.add.fail"));
    }

    public void addEnchantmentBase(ItemStack item, EnchantmentBase base, Player player, boolean takeMoney) {
        int level = getLevel(item, base);
        if (level > base.getMaxLevel()) {
            player.sendMessage(TranslatedMessage.translate("enchantment.max.message"));
            return;
        }
        long price = base.getDefaultPrice(level);
        EnchantmentPlayer enchantmentPlayer = playerHandler.getPlayer(player);
        if (enchantmentPlayer.getGems() < price) {
            player.sendMessage(TranslatedMessage.translate("enchantment.bought.fail", getPriceString(enchantmentPlayer, level, base)));
            return;
        }
        if (takeMoney)
            enchantmentPlayer.addGems(-price);
        player.sendMessage(TranslatedMessage.translate("enchantment.bought.success", base.getName(), "" + level));
        if (base instanceof VanillaEnchant) {
            item.addEnchantment(((VanillaEnchant) base).getEnchantment(), level);
            updateSigns(level, base, signs, enchantmentPlayer);
            return;
        }
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.addEnchant(base, level + 1, true);
        updateLore(meta, level, enchantmentPlayer, base);
        item.setItemMeta(meta);
        listenerHandler.onEnchant(item, base, player);
        updateSigns(level, base, signs, enchantmentPlayer);
    }

    private void updateLore(ItemMeta meta, int level, EnchantmentPlayer player, EnchantmentBase base) {
        List<String> lore = meta.getLore();
        if (lore == null)
            lore = new ArrayList<>();
        String temp = null;
        String priceMessage = TranslatedMessage.translate("enchantment.price");
        if (!lore.isEmpty() && lore.get(lore.size() - 1).startsWith(priceMessage)) {
            temp = lore.get(lore.size() - 1);
            lore.remove(temp);
        }
        if (level != 0)
            lore.remove(ChatColor.GRAY + base.getName() + " " + EnchantmentGUIListener.getRomanNumeral(level - 1));
        lore.add(ChatColor.GRAY + base.getName() + " " + EnchantmentGUIListener.getRomanNumeral(level));
        if (temp != null) {
            int currentPrice = Integer.parseInt(temp.substring(9, temp.length() - 1));
            if (player.usingGems())
                temp = priceMessage + (currentPrice + base.getDefaultPrice(level + 1)) + "G";
            else
                temp = priceMessage + TranslatedMessage.translate("dollar.symbol", "" + (currentPrice + base.getDefaultPrice(level)));
            lore.add(temp);
        }
        meta.setLore(lore);
    }

    public static int getLevel(ItemStack item, EnchantmentBase enchantment) {
        if(enchantment instanceof VanillaEnchant) {
            if(item.containsEnchantment(((VanillaEnchant) enchantment).getEnchantment()))
                return item.getEnchantmentLevel(((VanillaEnchant) enchantment).getEnchantment())+1;
            return enchantment.getStartLevel();
        }
        for (Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
            if (entry.getKey().getKey().equals(enchantment.getKey())) {
                return entry.getValue();
            }
        }
        return enchantment.getStartLevel();
    }

    private String getPriceString(EnchantmentPlayer player, int level, EnchantmentBase base) {
        if (player.usingGems())
            return base.getDefaultPrice(level) + "G";
        else
            return TranslatedMessage.translate("dollar.symbol", "" + base.getDefaultPrice(level));
    }

    private void updateSigns(int level, EnchantmentBase base, List<Location> signs, EnchantmentPlayer player) {
        for (Location location : signs)
            if (level >= base.getMaxLevel())
                player.getPlayer().sendSignChange(location, new String[]{"[" + TranslatedMessage.translate("enchantment") + "]", base.getName(), TranslatedMessage.translate("enchantment.price.maxed"), ""});
            else {
                player.getPlayer().sendSignChange(location, new String[]{"[" + TranslatedMessage.translate("enchantment") + "]", base.getName(), getPriceString(player, level, base), ""});
            }
    }
}
