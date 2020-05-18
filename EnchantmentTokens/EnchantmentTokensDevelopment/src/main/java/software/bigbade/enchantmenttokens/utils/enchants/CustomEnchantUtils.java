/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.utils.enchants;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.api.VanillaEnchant;
import software.bigbade.enchantmenttokens.localization.TranslatedPriceMessage;
import software.bigbade.enchantmenttokens.localization.TranslatedStringMessage;
import software.bigbade.enchantmenttokens.utils.RomanNumeralConverter;
import software.bigbade.enchantmenttokens.utils.listeners.EnchantListenerHandler;
import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

    @Override
    public void addEnchantment(ItemStack itemStack, String name, Player player) {
        EnchantmentPlayer enchantmentPlayer = playerHandler.getPlayer(player);
        for (EnchantmentBase base : handler.getAllEnchants()) {
            if (base.getEnchantmentName().equals(name) && base.canEnchantItem(itemStack)) {
                enchantmentPlayer.addGems(-addEnchantmentBase(itemStack, base, enchantmentPlayer));
                triggerOnEnchant(itemStack, base, player);
                return;
            }
        }
        player.sendMessage(new TranslatedStringMessage(enchantmentPlayer.getLanguage(), StringUtils.COMMAND_ERROR_NO_ENCHANTMENT).translate());
    }

    @Override
    public long addEnchantmentBase(ItemStack item, EnchantmentBase base, EnchantmentPlayer enchantmentPlayer) {
        int level = getNextLevel(item, base);
        if (level > base.getMaxLevel()) {
            enchantmentPlayer.getPlayer().sendMessage(new TranslatedStringMessage(enchantmentPlayer.getLanguage(), StringUtils.MAXED_MESSAGE).translate());
            return 0;
        }
        long price = base.getDefaultPrice(level);

        if (enchantmentPlayer.getGems() < price) {
            enchantmentPlayer.getPlayer().sendMessage(new TranslatedStringMessage(enchantmentPlayer.getLanguage(), StringUtils.ENCHANTMENT_BOUGHT_FAIL).translate(new TranslatedPriceMessage(enchantmentPlayer.getLanguage()).translate("" + base.getDefaultPrice(level))));
            return 0;
        }
        enchantmentPlayer.getPlayer().sendMessage(new TranslatedStringMessage(enchantmentPlayer.getLanguage(), StringUtils.ENCHANTMENT_BOUGHT_SUCCESS).translate(base.getEnchantmentName(), "" + level));
        if (base instanceof VanillaEnchant) {
            item.addEnchantment(base.getEnchantment(), level);
            updateSigns(base, signs, enchantmentPlayer);
            return price;
        }
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.addEnchant(base.getEnchantment(), level, true);
        updateLore(meta, level, base);
        item.setItemMeta(meta);
        updateSigns(base, signs, enchantmentPlayer);
        return price;
    }

    public void triggerOnEnchant(ItemStack item, EnchantmentBase base, Player player) {
        listenerHandler.onEnchant(item, base, player);
    }

    private void updateLore(ItemMeta meta, int level, EnchantmentBase base) {
        List<String> lore = meta.getLore();
        if (lore == null)
            lore = new ArrayList<>();
        if (level != 0)
            lore.remove(ChatColor.GRAY + base.getEnchantmentName() + " " + RomanNumeralConverter.getRomanNumeral(level - 1));
        lore.add(ChatColor.GRAY + base.getEnchantmentName() + " " + RomanNumeralConverter.getRomanNumeral(level));
        meta.setLore(lore);
    }

    @Override
    public int getNextLevel(ItemStack item, EnchantmentBase enchantment) {
        if (enchantment instanceof VanillaEnchant)
            return item.getEnchantmentLevel(enchantment.getEnchantment()) + 1;
        if (item.getItemMeta() == null || !item.getItemMeta().hasEnchants()) return enchantment.getStartLevel();
        if (!item.getItemMeta().hasEnchant(enchantment.getEnchantment())) {
            return 1;
        }
        return item.getItemMeta().getEnchants().get(enchantment.getEnchantment()) + 1;
    }

    private void updateSigns(EnchantmentBase base, Set<Location> signs, EnchantmentPlayer player) {
        for (Location location : signs)
            player.getPlayer().sendSignChange(location, new String[]{"[" + new TranslatedStringMessage(Locale.getDefault(), StringUtils.ENCHANTMENT) + "]", base.getEnchantmentName(), "", ""});
    }
}
