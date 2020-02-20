package software.bigbade.enchantmenttokens.gui;

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

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.localization.TranslatedMessage;
import software.bigbade.enchantmenttokens.utils.EnchantButton;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantUtils;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;

import java.util.*;

public class EnchantmentMenuFactory {
    private ItemStack glassPane;
    private int version;
    private EnchantmentPlayerHandler handler;
    private EnchantmentHandler enchantmentHandler;
    private EnchantUtils utils;

    //Barrier used for exiting the GUI
    private ItemStack exit;

    public EnchantmentMenuFactory(EnchantmentTokens main) {
        this.version = main.getVersion();
        this.handler = main.getPlayerHandler();
        this.utils = main.getUtils();
        this.enchantmentHandler = main.getEnchantmentHandler();

        exit = makeItem(Material.BARRIER, TranslatedMessage.translate("enchant.back"));
        glassPane = makeItem(Material.BLACK_STAINED_GLASS_PANE, " ");
    }

    /**
     * Generate the GUI with every enchantment in it
     *
     * @param player Target player
     * @return Generated enchantment inventory
     */
    public SubInventory generateGUI(EnchantmentPlayer player) {
        ItemStack itemStack = player.getPlayer().getInventory().getItemInMainHand();
        Inventory inventory = Bukkit.createInventory(null, 54, itemStack.getType().name().replace("_", " ").toLowerCase() + " " + TranslatedMessage.translate("tool.enchants"));
        SubInventory subInventory = new SubInventory(inventory);

        subInventory.setItem(itemStack);

        for (int i = 0; i < 54; i++) {
            if (i < 10 || i > 45 || i % 9 == 0 || i % 9 == 8)
                inventory.setItem(i, glassPane);
        }

        inventory.setItem(4, itemStack);

        inventory.setItem(49, exit);

        addItems(subInventory, player);

        subInventory.addButton(new EnchantButton(exit, item -> genItemInventory(player.getPlayer(), subInventory.getItem())), 49);
        return subInventory;
    }

    private void addItems(SubInventory subInventory, EnchantmentPlayer player) {
        int next = subInventory.getInventory().firstEmpty();
        ItemStack item = player.getPlayer().getInventory().getItemInMainHand();

        for (EnchantmentBase enchantment : enchantmentHandler.getAllEnchants()) {
            if (!enchantment.canEnchantItem(item))
                continue;
            EnchantButton button = updateItem(enchantment, item, player);
            subInventory.addButton(button, next);
            subInventory.getInventory().setItem(next, button.getItem());
            next = subInventory.getInventory().firstEmpty();
        }
    }

    private EnchantButton updateItem(EnchantmentBase base, ItemStack stack, EnchantmentPlayer player) {
        ItemStack item = EnchantmentMenuFactory.makeItem(base.getIcon(), ChatColor.GREEN + base.getName());
        int level = addLore(stack, base, item, player.usingGems());
        if (level <= base.getMaxLevel()) {
            item.setAmount(level);
            return new EnchantButton(item, itemStack -> {
                utils.addEnchantmentBase(itemStack, base, player.getPlayer(), false);
                updatePriceStr(base, level, itemStack);
                return generateGUI(player);
            });
        } else {
            item.setAmount(64);
            return new EnchantButton(item, itemStack -> generateGUI(player));
        }
    }

    private int addLore(ItemStack item, EnchantmentBase base, ItemStack target, boolean gems) {
        int level = getLevel(item, base);
        ItemMeta meta = target.getItemMeta();
        assert meta != null;
        String priceStr = EnchantUtils.getPriceString(gems, level, base);
        String levelStr = TranslatedMessage.translate("enchantment.level");
        if (level <= base.getMaxLevel())
            levelStr += level;
        else
            levelStr += TranslatedMessage.translate("enchantment.maxed");

        if (level > base.maxLevel)
            meta.setLore(Collections.singletonList(levelStr));
        else
            meta.setLore(Arrays.asList(levelStr, priceStr));

        target.setItemMeta(meta);
        return level;
    }

    private void updatePriceStr(EnchantmentBase base, int level, ItemStack item) {
        assert item.getItemMeta() != null;
        List<String> lore = item.getItemMeta().getLore();
        assert lore != null;
        String priceStr = lore.get(lore.size() - 1);
        long price = Long.parseLong(priceStr.replaceAll("[^\\d.]", ""));
        long newPrice = price + base.getDefaultPrice(level);
        priceStr = priceStr.replace("" + price, "" + newPrice);
        lore.set(lore.size() - 1, TranslatedMessage.translate("enchantment.price") + priceStr);
    }

    private int getLevel(ItemStack stack, EnchantmentBase base) {
        for (Map.Entry<Enchantment, Integer> enchantment : stack.getEnchantments().entrySet()) {
            if (enchantment.getKey().getKey().equals(base.getKey()))
                return enchantment.getValue();
        }
        return 1;
    }

    public EnchantmentGUI genInventory(Player player) {
        return genItemInventory(player, player.getInventory().getItemInMainHand());
    }

    public EnchantmentGUI genItemInventory(Player player, ItemStack item) {
        Inventory inventory = Bukkit.createInventory(null, 27, "Enchantments");
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
        EnchantmentPlayer enchantPlayer = handler.getPlayer(player);
        if (lore.isEmpty() || !lore.get(lore.size() - 1).startsWith(TranslatedMessage.translate("enchantment.price"))) {
            if (enchantPlayer.usingGems())
                lore.add(TranslatedMessage.translate("enchantment.price") + "0G");
            else
                lore.add(TranslatedMessage.translate("enchantment.price") + " " + TranslatedMessage.translate("dollar.symbol", "0"));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        EnchantmentGUI enchantInv = new EnchantmentGUI(inventory);
        populate(enchantInv, item, player);
        int i = inventory.firstEmpty();
        while (i > -1 && i < 28) {
            inventory.setItem(i, glassPane);
            i = inventory.firstEmpty();
        }
        enchantPlayer.setCurrentGUI(null);
        player.openInventory(inventory);
        enchantPlayer.setCurrentGUI(enchantInv);
        return enchantInv;
    }

    /*
    Order:
    9: (14+) Crossbow
    10: (13+) Trident (12-9) Fishing Rod
    11: tools
    12: sword
    13: (13+) Fishing rod (-8) Fishing Rod
    14: Armor
    15: Bow
    16: (14+) Fishing Rod (9+) Shield
    17: (14+) Shield
    21: Enchant
    23: Cancel
     */
    private void populate(EnchantmentGUI inventory, ItemStack item, Player player) {
        inventory.getInventory().setItem(4, item);

        if (version >= 14)
            generateButton(inventory, EnchantmentTarget.CROSSBOW, Material.CROSSBOW, "tool.crossbow", 9);
        if (version >= 13)
            generateButton(inventory, EnchantmentTarget.TRIDENT, Material.TRIDENT, "tool.trident", 10);
        else if (version >= 9)
            generateButton(inventory, EnchantmentTarget.FISHING_ROD, Material.FISHING_ROD, "tool.fishingrod", 10);
        generateButton(inventory, EnchantmentTarget.TOOL, Material.DIAMOND_PICKAXE, "tool.tool", 11);
        generateButton(inventory, EnchantmentTarget.WEAPON, Material.DIAMOND_SWORD, "tool.sword", 12);
        if (version >= 13 || version < 8)
            generateButton(inventory, EnchantmentTarget.FISHING_ROD, Material.FISHING_ROD, "tool.fishingrod", 13);
        generateButton(inventory, EnchantmentTarget.ARMOR, Material.DIAMOND_CHESTPLATE, "tool.armor", 14);
        generateButton(inventory, EnchantmentTarget.BOW, Material.BOW, "tool.bow", 15);
        if (version >= 14)
            generateButton(inventory, EnchantmentTarget.FISHING_ROD, Material.FISHING_ROD, "tool.fishingrod", 16);
        else if (version >= 9)
            generateButton(inventory, null, Material.SHIELD, "tool.shield", 16);
        if (version >= 14)
            generateButton(inventory, null, Material.SHIELD, "tool.shield", 17);
        ItemStack newItem = makeItem(Material.REDSTONE_BLOCK, TranslatedMessage.translate("enchant.cancel"));
        inventory.addButton(new EnchantButton(newItem, itemStack -> null), 24);
        inventory.getInventory().setItem(23, newItem);

        newItem = makeItem(Material.EMERALD_BLOCK, TranslatedMessage.translate("enchant.confirm"));
        inventory.addButton(new EnchantButton(newItem, itemStack -> {
            PlayerInventory playerInventory = player.getInventory();
            removePriceLine(itemStack, handler.getPlayer(player));
            playerInventory.setItem(playerInventory.getHeldItemSlot(), itemStack);
            return null;
        }), 21);
        inventory.getInventory().setItem(21, newItem);
    }

    private void removePriceLine(ItemStack item, EnchantmentPlayer player) {
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        if (meta.getLore() != null) {
            String line = meta.getLore().get(meta.getLore().size() - 1);
            long price = Long.parseLong(line.substring(9).replace("G", "").replace(TranslatedMessage.translate("dollar.symbol", ""), ""));
            List<String> lore = meta.getLore();
            lore.remove(meta.getLore().size() - 1);
            meta.setLore(lore);

            item.setItemMeta(meta);
            player.addGems(-price);
            player.getPlayer().getInventory().setItemInMainHand(item);
        }
    }

    private void generateButton(EnchantmentGUI inventory, EnchantmentTarget target, Material material, String key, int slot) {
        ItemStack item = makeItem(material, TranslatedMessage.translate(key));
        inventory.addButton(new EnchantButton(item, itemStack -> {
            if ((target != null && target.includes(inventory.getOpener().getInventory().getItemInMainHand())) || inventory.getOpener().getInventory().getItemInMainHand().getType() == material)
                return generateGUI(handler.getPlayer(inventory.getOpener()));
            else
                return genItemInventory(inventory.getOpener(), itemStack);
        }), slot);
        inventory.getInventory().setItem(slot, item);
    }

    public static ItemStack makeItem(Material material, String name) {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        stack.setItemMeta(meta);
        return stack;
    }
}
