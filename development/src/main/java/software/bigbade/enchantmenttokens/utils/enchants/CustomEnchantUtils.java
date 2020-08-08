/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.utils.enchants;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.api.VanillaEnchant;
import software.bigbade.enchantmenttokens.localization.TranslatedPriceMessage;
import software.bigbade.enchantmenttokens.localization.TranslatedStringMessage;
import software.bigbade.enchantmenttokens.utils.ReflectionManager;
import software.bigbade.enchantmenttokens.utils.RomanNumeralConverter;
import software.bigbade.enchantmenttokens.utils.listeners.EnchantListenerHandler;
import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class CustomEnchantUtils extends EnchantUtils {
    private final EnchantmentHandler handler;
    private final EnchantmentPlayerHandler playerHandler;
    private final EnchantListenerHandler listenerHandler;
    private final Set<Location> signs;

    public CustomEnchantUtils(EnchantmentHandler handler, EnchantmentPlayerHandler playerHandler, EnchantListenerHandler listenerHandler, Set<Location> signs) {
        setInstance(this);
        this.handler = handler;
        this.playerHandler = playerHandler;
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
    public void addEnchantment(ItemStack itemStack, String name, Player player) {
        EnchantmentPlayer enchantmentPlayer = playerHandler.getPlayer(player);
        for (EnchantmentBase base : handler.getAllEnchants()) {
            if (base.getEnchantmentName().equals(name) && base.canEnchantItem(itemStack)) {
                enchantmentPlayer.getGems().thenAccept(gems -> {
                    int level = getLevel(itemStack, base) + 1;
                    long price = base.getDefaultPrice(level);
                    if (gems < price) {
                        enchantmentPlayer.getPlayer().sendMessage(new TranslatedStringMessage(enchantmentPlayer.getLanguage(), StringUtils.ENCHANTMENT_BOUGHT_FAIL).translate(new TranslatedPriceMessage(enchantmentPlayer.getLanguage()).translate("" + base.getDefaultPrice(level))));
                    } else {
                        addEnchantmentBase(itemStack, base, enchantmentPlayer);
                        enchantmentPlayer.addGems(-price);
                        triggerOnEnchant(itemStack, base, player);
                    }
                });
                return;
            }
        }
        player.sendMessage(new TranslatedStringMessage(enchantmentPlayer.getLanguage(), StringUtils.COMMAND_ERROR_NO_ENCHANTMENT).translate());
    }

    @Override
    public void addEnchantmentBase(ItemStack item, EnchantmentBase base, EnchantmentPlayer enchantmentPlayer) {
        int level = getLevel(item, base) + 1;
        if (level > base.getMaxLevel()) {
            enchantmentPlayer.getPlayer().sendMessage(new TranslatedStringMessage(enchantmentPlayer.getLanguage(), StringUtils.MAXED_MESSAGE).translate());
            return;
        }

        enchantmentPlayer.getPlayer().sendMessage(new TranslatedStringMessage(enchantmentPlayer.getLanguage(), StringUtils.ENCHANTMENT_BOUGHT_SUCCESS).translate(base.getEnchantmentName(), "" + level));
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
