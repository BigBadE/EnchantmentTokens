/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.gui;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.api.VanillaEnchant;
import software.bigbade.enchantmenttokens.localization.TranslatedPriceMessage;
import software.bigbade.enchantmenttokens.localization.TranslatedStringMessage;
import software.bigbade.enchantmenttokens.utils.ItemUtils;
import software.bigbade.enchantmenttokens.utils.ReflectionManager;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantUtils;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public class CustomMenuFactory implements EnchantmentMenuFactory {
    private final ItemStack glassPane = ItemUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " ");
    private final EnchantmentPlayerHandler handler;
    private final EnchantmentHandler enchantmentHandler;
    private final EnchantUtils utils;

    private final List<EnchantButton> buttons = new ArrayList<>();

    private static final Pattern REMOVE_LETTERS = Pattern.compile("[^\\d.]");

    public CustomMenuFactory(EnchantmentPlayerHandler handler, EnchantUtils utils, EnchantmentHandler enchantmentHandler) {
        this.handler = handler;
        this.utils = utils;
        this.enchantmentHandler = enchantmentHandler;

        generateButtons();
    }

    public synchronized void addButtons(List<EnchantButton> button) {
        buttons.addAll(button);
    }

    /*
    Order:
    9: (14+) Crossbow
    10: (13+) Trident (12-9) Fishing Rod
    11: tools
    12: sword
    13: (13) Fishing rod (-8) Fishing Rod
    14: Armor
    15: Bow
    16: (14+) Fishing Rod (9+) Shield
    17: (14+) Shield
     */
    private void generateButtons() {
        addHeaderButtons();
        if (ReflectionManager.VERSION == 13 || ReflectionManager.VERSION < 8)
            generateButton(EnchantmentTarget.FISHING_ROD, Material.FISHING_ROD, StringUtils.TOOL_FISHING_ROD);
        generateButton(EnchantmentTarget.ARMOR, Material.DIAMOND_CHESTPLATE, StringUtils.TOOL_ARMOR);
        generateButton(EnchantmentTarget.BOW, Material.BOW, StringUtils.TOOL_BOW);
        addFooterButtons();
    }

    private void addHeaderButtons() {
        if (ReflectionManager.VERSION >= 14)
            generateButton(EnchantmentTarget.CROSSBOW, Material.CROSSBOW, StringUtils.TOOL_CROSSBOW);
        if (ReflectionManager.VERSION >= 13)
            generateButton(EnchantmentTarget.TRIDENT, Material.TRIDENT, StringUtils.TOOL_TRIDENT);
        else if (ReflectionManager.VERSION >= 9)
            generateButton(EnchantmentTarget.FISHING_ROD, Material.FISHING_ROD, StringUtils.TOOL_FISHING_ROD);
        generateButton(EnchantmentTarget.TOOL, Material.DIAMOND_PICKAXE, StringUtils.TOOL_TOOLS);
        generateButton(EnchantmentTarget.WEAPON, Material.DIAMOND_SWORD, StringUtils.TOOL_SWORD);
    }

    private void addFooterButtons() {
        if (ReflectionManager.VERSION >= 14)
            generateButton(EnchantmentTarget.FISHING_ROD, Material.FISHING_ROD, StringUtils.TOOL_FISHING_ROD);
        else if (ReflectionManager.VERSION >= 9)
            generateButton(null, Material.SHIELD, StringUtils.TOOL_SHIELD);
        if (ReflectionManager.VERSION >= 14) {
            for (int i = 0; i < 3; i++)
                buttons.add(EnchantmentTokens.getEmptyButton());
            generateButton(null, Material.SHIELD, StringUtils.TOOL_SHIELD);
            for (int i = 0; i < 3; i++)
                buttons.add(EnchantmentTokens.getEmptyButton());
        }
    }

    /**
     * Generate the GUI with every enchantment in it
     *
     * @param itemStack Item target
     * @param player    Target player
     * @return Generated enchantment inventory
     */
    public SubInventory generateGUI(ItemStack itemStack, EnchantmentPlayer player) {
        Inventory inventory = Bukkit.createInventory(null, 54, getName(itemStack, player.getLanguage()));
        SubInventory subInventory = new SubInventory(inventory, player.getCurrentGUI().getAddedEnchants());

        subInventory.setItem(itemStack);

        for (int i = 0; i < 54; i++) {
            if (i < 10 || i > 45 || i % 9 == 0 || i % 9 == 8)
                inventory.setItem(i, glassPane);
        }

        inventory.setItem(4, itemStack);

        addItems(subInventory, player);

        subInventory.addButton(new CustomEnchantButton(ItemUtils.createItem(Material.BARRIER, new TranslatedStringMessage(player.getLanguage(), StringUtils.GUI_BACK).translate()), item -> genItemInventory(player, subInventory.getItem(), subInventory.getAddedEnchants())), 49);
        return subInventory;
    }

    private void addItems(SubInventory subInventory, EnchantmentPlayer player) {
        int next = subInventory.getInventory().firstEmpty();
        ItemStack item = subInventory.getItem();

        for (EnchantmentBase enchantment : enchantmentHandler.getAllEnchants()) {
            if (!enchantment.canEnchantItem(item)) {
                continue;
            }
            EnchantButton button = updateItem(enchantment, item, player);
            subInventory.addButton(button, next);
            next = subInventory.getInventory().firstEmpty();
        }
    }

    private EnchantButton updateItem(EnchantmentBase base, ItemStack stack, EnchantmentPlayer player) {
        ItemStack item = ItemUtils.createItem(base.getIcon(), ChatColor.GREEN + base.getEnchantmentName());
        int level = utils.getLevel(stack, base)+1;
        addLore(base, item, level, player.getLanguage());
        if (level <= base.getMaxLevel()) {
            return new CustomEnchantButton(item, enchantmentPlayer -> {
                ItemStack itemStack = enchantmentPlayer.getCurrentGUI().getItem();
                long price = utils.addEnchantmentBase(itemStack, base, player);
                player.getCurrentGUI().addEnchantment(base);
                if (price == 0)
                    return generateGUI(itemStack, player);
                if (!(base instanceof VanillaEnchant))
                    swapLines(itemStack);
                updatePriceStr(price, itemStack);
                return generateGUI(itemStack, player);
            });
        } else {
            item.setAmount(64);
            return new CustomEnchantButton(item, enchantmentPlayer -> generateGUI(enchantmentPlayer.getCurrentGUI().getItem(), enchantmentPlayer));
        }
    }

    private void swapLines(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        List<String> lore = meta.getLore();
        assert lore != null;
        String temp = lore.get(lore.size() - 2);
        lore.set(lore.size() - 2, lore.get(lore.size() - 1));
        lore.set(lore.size() - 1, temp);
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
    }

    private void addLore(EnchantmentBase base, ItemStack target, int level, Locale locale) {
        ItemMeta meta = target.getItemMeta();
        assert meta != null;
        String priceStr = ChatColor.GRAY + new TranslatedStringMessage(locale, StringUtils.PRICE).translate(new TranslatedPriceMessage(locale).translate("" + base.getDefaultPrice(level)));
        String levelStr;
        if (level <= base.getMaxLevel())
            levelStr = new TranslatedStringMessage(locale, StringUtils.LEVEL).translate("" + level);
        else
            levelStr = new TranslatedStringMessage(locale, StringUtils.LEVEL).translate(new TranslatedStringMessage(locale, StringUtils.MAXED).translate());

        if (level > base.getMaxLevel())
            meta.setLore(Collections.singletonList(levelStr));
        else
            meta.setLore(Arrays.asList(levelStr, priceStr));

        target.setItemMeta(meta);
    }

    private void updatePriceStr(long price, ItemStack item) {
        assert item.getItemMeta() != null;
        List<String> lore = item.getItemMeta().getLore();
        assert lore != null;
        String priceStr = ChatColor.stripColor(lore.get(lore.size() - 1));
        long oldPrice = Long.parseLong(REMOVE_LETTERS.matcher(priceStr).replaceAll(""));
        long newPrice = price + oldPrice;
        priceStr = priceStr.replace("" + oldPrice, "" + newPrice);
        lore.set(lore.size() - 1, ChatColor.GRAY + priceStr);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public EnchantmentGUI genInventory(Player player) {
        return genItemInventory(handler.getPlayer(player), player.getInventory().getItemInMainHand().clone(), new ArrayList<>());
    }

    private String getName(ItemStack item, Locale locale) {
        return new TranslatedStringMessage(locale, StringUtils.TOOL_ENCHANTS).translate(VanillaEnchant.capitalizeString(item.getType().name().toLowerCase().replace("_", " ")));
    }

    public EnchantmentGUI genItemInventory(EnchantmentPlayer enchantPlayer, ItemStack item, List<EnchantmentBase> added) {
        Inventory inventory = Bukkit.createInventory(null, 9 * (2 + ((int) Math.ceil(buttons.size() / 7f))), getName(item, enchantPlayer.getLanguage()));
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            if (item.getType() == Material.AIR) {
                return null;
            } else {
                meta = Bukkit.getItemFactory().getItemMeta(item.getType());
            }
        }
        assert meta != null;
        List<String> lore = meta.getLore();
        if (lore == null)
            lore = new ArrayList<>();
        addPriceLine(lore, enchantPlayer.getLanguage());

        meta.setLore(lore);
        item.setItemMeta(meta);
        EnchantmentGUI enchantInv = new CustomEnchantmentGUI(inventory, added);
        enchantInv.setItem(item);
        populate(enchantInv, item, enchantPlayer.getPlayer());
        enchantPlayer.setCurrentGUI(null);
        enchantPlayer.getPlayer().openInventory(inventory);
        enchantPlayer.setCurrentGUI(enchantInv);
        return enchantInv;
    }

    private void addPriceLine(List<String> lore, Locale locale) {
        if (lore.isEmpty()) {
            lore.add(new TranslatedStringMessage(locale, StringUtils.PRICE).translate(new TranslatedPriceMessage(locale).translate("0")));
        } else {
            if (!lore.get(lore.size() - 1).startsWith(new TranslatedStringMessage(locale, StringUtils.PRICE).translate("")))
                lore.add(new TranslatedStringMessage(locale, StringUtils.PRICE).translate(new TranslatedPriceMessage(locale).translate("0")));
        }
    }

    private void populate(EnchantmentGUI inventory, ItemStack item, Player player) {
        inventory.getInventory().setItem(4, item);
        Locale locale = handler.getPlayer(player).getLanguage();
        int rows = 2 + (int) Math.ceil(buttons.size() / 7f);
        for (int i = 1; i < rows - 1; i++) {
            for (int i2 = 0; i2 < Math.min(7, buttons.size() - (i - 1) * 7); i2++) {
                EnchantButton button = buttons.get((i - 1) * 7 + i2);
                translate(locale, button);
                inventory.addButton(button, i * 9 + i2 + 1);
            }
        }
        ItemStack newItem = ItemUtils.createItem(Material.REDSTONE_BLOCK, new TranslatedStringMessage(locale, StringUtils.GUI_CANCEL).translate());
        inventory.addButton(new CustomEnchantButton(newItem, itemStack -> null), rows * 9 - 4);

        newItem = ItemUtils.createItem(Material.EMERALD_BLOCK, new TranslatedStringMessage(locale, StringUtils.GUI_CONFIRM).translate());

        inventory.addButton(new CustomEnchantButton(newItem, enchantmentPlayer -> {
            PlayerInventory playerInventory = player.getInventory();
            removePriceLine(enchantmentPlayer.getCurrentGUI().getItem(), enchantmentPlayer);
            playerInventory.setItem(playerInventory.getHeldItemSlot(), enchantmentPlayer.getCurrentGUI().getItem());
            for (EnchantmentBase base : enchantmentPlayer.getCurrentGUI().getAddedEnchants())
                utils.triggerOnEnchant(enchantmentPlayer.getCurrentGUI().getItem(), base, enchantmentPlayer.getPlayer());
            return null;
        }), rows * 9 - 6);

        int i = inventory.getInventory().firstEmpty();
        while (i != -1) {
            inventory.getInventory().setItem(i, glassPane);
            i = inventory.getInventory().firstEmpty();
        }
    }

    private void translate(Locale locale, EnchantButton button) {
        Objects.requireNonNull(button.getItem().getItemMeta());
        String name = button.getTranslationString();
        if (name == null) {
            return;
        }
        ItemStack item = ItemUtils.createItem(button.getItem().getType(), new TranslatedStringMessage(locale, name).translate());
        button.setItem(item);
    }

    private void removePriceLine(ItemStack item, EnchantmentPlayer player) {
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        if (meta.getLore() != null) {
            String line = ChatColor.stripColor(meta.getLore().get(meta.getLore().size() - 1));
            long price = Long.parseLong(REMOVE_LETTERS.matcher(line).replaceAll(""));
            List<String> lore = meta.getLore();
            lore.remove(meta.getLore().size() - 1);
            meta.setLore(lore);

            item.setItemMeta(meta);
            player.addGems(-price);
            player.getPlayer().getInventory().setItemInMainHand(item);
        }
    }

    private void generateButton(EnchantmentTarget target, Material material, String name) {
        ItemStack item = new ItemStack(material);
        buttons.add(
                new CustomEnchantButton(item, player -> {
                    if ((target != null && target.includes(player.getCurrentGUI().getItem())) || player.getCurrentGUI().getItem().getType() == material)
                        return generateGUI(player.getCurrentGUI().getItem(), player);
                    else
                        return genItemInventory(player, player.getCurrentGUI().getItem(), player.getCurrentGUI().getAddedEnchants());
                }, name));
    }
}
