/*
 * Custom enchantments for Minecraft
 * Copyright (C) 2021 Big_Bad_E
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bigbade.enchantmenttokens.utils.enchants;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.bigbade.enchantmenttokens.api.EnchantmentBase;
import com.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import com.bigbade.enchantmenttokens.api.VanillaEnchant;
import com.bigbade.enchantmenttokens.localization.LocaleMessages;
import com.bigbade.enchantmenttokens.utils.ReflectionManager;
import com.bigbade.enchantmenttokens.utils.RomanNumeralConverter;
import com.bigbade.enchantmenttokens.utils.listeners.EnchantListenerHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class CustomEnchantUtils extends EnchantUtils {
    private final EnchantListenerHandler listenerHandler;
    private final Set<Location> signs;

    public CustomEnchantUtils(EnchantListenerHandler listenerHandler, Set<Location> signs) {
        setInstance(this);
        this.listenerHandler = listenerHandler;
        this.signs = signs;
    }

    private static void updateSigns(Set<Location> signs, Player player) {
        for (Location location : signs) {
            Sign sign = (Sign) location.getBlock().getState();
            player.sendSignChange(location, new String[]{sign.getLine(0), sign.getLine(1), "", ""});
        }
    }

    @Override
    public void addEnchantment(EnchantmentPlayer enchantmentPlayer, EnchantmentHandler handler,
                               ItemStack itemStack, String name) {
        for (EnchantmentBase base : handler.getAllEnchants()) {
            if (base.getEnchantmentName().equals(name) && base.canEnchantItem(itemStack)) {
                enchantmentPlayer.getGems().thenAccept(gems -> {
                    int level = getLevel(itemStack, base) + 1;
                    long price = base.getDefaultPrice(level);
                    if (gems < price) {
                        enchantmentPlayer.getPlayer().sendMessage(
                                LocaleMessages.ENCHANTMENT_BOUGHT_FAIL.translate(enchantmentPlayer.getLanguage(),
                                        LocaleMessages.translatePrice(enchantmentPlayer.getLanguage(),
                                                base.getDefaultPrice(level))));
                    } else {
                        addEnchantmentBase(itemStack, base, enchantmentPlayer);
                        enchantmentPlayer.addGems(-price);
                        triggerOnEnchant(itemStack, base, enchantmentPlayer.getPlayer());
                    }
                });
                return;
            }
        }
        enchantmentPlayer.getPlayer()
                .sendMessage(LocaleMessages.COMMAND_ERROR_NO_ENCHANTMENT.translate(enchantmentPlayer.getLanguage()));
    }

    @Override
    public void addEnchantmentBase(ItemStack item, EnchantmentBase base, EnchantmentPlayer enchantmentPlayer) {
        int level = getLevel(item, base) + 1;
        if (level > base.getMaxLevel()) {
            enchantmentPlayer.getPlayer()
                    .sendMessage(LocaleMessages.MAXED_MESSAGE.translate(enchantmentPlayer.getLanguage()));
            return;
        }

        enchantmentPlayer.getPlayer().sendMessage(LocaleMessages.ENCHANTMENT_BOUGHT_SUCCESS
                .translate(enchantmentPlayer.getLanguage(), base.getEnchantmentName(), level));
        addEnchantmentBase(item, base, enchantmentPlayer.getPlayer(), level);
    }

    @Override
    public void addEnchantmentBaseNoMessages(ItemStack item, EnchantmentBase base, Player player, int level) {
        level = Math.min(base.getMaxLevel(), Math.max(level + 1, base.getStartLevel()));
        addEnchantmentBase(item, base, player, level);
    }

    @Override
    public void removeEnchantmentBase(ItemStack item, EnchantmentBase base) {
        int level = getLevel(item, base) - 1;
        Objects.requireNonNull(item.getItemMeta()).removeEnchant(base.getEnchantment());
        if (base instanceof VanillaEnchant) {
            return;
        }
        List<String> lore = Objects.requireNonNull(item.getItemMeta()).getLore();
        if (lore == null || lore.isEmpty()) {
            return;
        }
        if (level == 1) {
            lore.remove(base.getEnchantmentName());
        } else {
            lore.remove(base.getEnchantmentName() + " " + RomanNumeralConverter.getRomanNumeral(level));
        }
    }

    public void addEnchantmentBase(ItemStack item, EnchantmentBase base, Player player, int level) {
        if (base instanceof VanillaEnchant) {
            item.addEnchantment(base.getEnchantment(), level);
            CustomEnchantUtils.updateSigns(signs, player);
            return;
        }
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.addEnchant(base.getEnchantment(), level, true);
        CustomEnchantUtils.updateLore(meta, level, base);
        item.setItemMeta(meta);
        CustomEnchantUtils.updateSigns(signs, player);
        triggerOnEnchant(item, base, player);
    }

    public void triggerOnEnchant(ItemStack item, EnchantmentBase base, Player player) {
        listenerHandler.onEnchant(item, base, player);
    }

    private static void updateLore(ItemMeta meta, int level, EnchantmentBase base) {
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        if (level != 0) {
            for (String line : lore) {
                if (line.startsWith(ChatColor.GRAY + base.getEnchantmentName())) {
                    lore.remove(line);
                    break;
                }
            }
        }
        if (level == 1 && ReflectionManager.VERSION >= 9) {
            lore.add(ChatColor.GRAY + base.getEnchantmentName());
        } else {
            lore.add(ChatColor.GRAY + base.getEnchantmentName() + " " + RomanNumeralConverter.getRomanNumeral(level));
        }
        meta.setLore(lore);
    }

    public int getLevel(ItemStack item, EnchantmentBase base) {
        if (base instanceof VanillaEnchant) {
            return item.getEnchantmentLevel(base.getEnchantment());
        }
        if (item.getItemMeta() == null || !item.getItemMeta().hasEnchants()) {
            return base.getStartLevel() - 1;
        }
        if (!item.getItemMeta().hasEnchant(base.getEnchantment())) {
            return base.getStartLevel() - 1;
        }
        return item.getItemMeta().getEnchants().get(base.getEnchantment());
    }
}
