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
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.api.VanillaEnchant;
import software.bigbade.enchantmenttokens.localization.TranslatedMessage;
import software.bigbade.enchantmenttokens.utils.EnchantButton;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantUtils;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EnchantmentMenuFactory implements MenuFactory {
    private ItemStack glassPane = makeItem(Material.BLACK_STAINED_GLASS_PANE, " ");
    private int version;
    private EnchantmentPlayerHandler handler;
    private EnchantmentHandler enchantmentHandler;
    private EnchantUtils utils;

    private static final String PRICE = "enchantment.price";

    //Barrier used for exiting the GUI
    private ItemStack exit = makeItem(Material.BARRIER, TranslatedMessage.translate("enchant.back"));
    private EnchantButton glassButton = new EnchantButton(glassPane, player -> genItemInventory(player, player.getCurrentGUI().getItem()));

    private List<EnchantButton> buttons = new ArrayList<>();

    public EnchantmentMenuFactory(int version, EnchantmentPlayerHandler handler, EnchantUtils utils, EnchantmentHandler enchantmentHandler) {
        this.version = version;
        this.handler = handler;
        this.utils = utils;
        this.enchantmentHandler = enchantmentHandler;

        generateButtons();
    }

    public void addButton(EnchantButton button) {
        buttons.add(button);
    }

    private static final String FISHINGROD = "tool.fishingrod";
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
        if (version >= 14)
            generateButton(EnchantmentTarget.CROSSBOW, Material.CROSSBOW, "tool.crossbow");
        if (version >= 13)
            generateButton(EnchantmentTarget.TRIDENT, Material.TRIDENT, "tool.trident");
        else if (version >= 9)
            generateButton(EnchantmentTarget.FISHING_ROD, Material.FISHING_ROD, FISHINGROD);
        generateButton(EnchantmentTarget.TOOL, Material.DIAMOND_PICKAXE, "tool.tool");
        generateButton(EnchantmentTarget.WEAPON, Material.DIAMOND_SWORD, "tool.sword");
        if (version == 13 || version < 8)
            generateButton(EnchantmentTarget.FISHING_ROD, Material.FISHING_ROD, FISHINGROD);
        generateButton(EnchantmentTarget.ARMOR, Material.DIAMOND_CHESTPLATE, "tool.armor");
        generateButton(EnchantmentTarget.BOW, Material.BOW, "tool.bow");
        if (version >= 14)
            generateButton(EnchantmentTarget.FISHING_ROD, Material.FISHING_ROD, FISHINGROD);
        else if (version >= 9)
            generateButton(null, Material.SHIELD, "tool.shield");
        if (version >= 14) {
            addEmptyItems(3);
            generateButton(null, Material.SHIELD, "tool.shield");
            addEmptyItems(3);
        }
    }

    private void addEmptyItems(int amount) {
        for(int i = 0; i < amount; i++)
            buttons.add(glassButton);
    }

    /**
     * Generate the GUI with every enchantment in it
     *
     * @param itemStack Item target
     * @param player    Target player
     * @return Generated enchantment inventory
     */
    public SubInventory generateGUI(ItemStack itemStack, EnchantmentPlayer player) {
        Inventory inventory = Bukkit.createInventory(null, 54, itemStack.getType().name().replace("_", " ").toLowerCase() + " " + TranslatedMessage.translate("tool.enchants"));
        SubInventory subInventory = new SubInventory(inventory);

        subInventory.setOpener(player);
        subInventory.setItem(itemStack);

        for (int i = 0; i < 54; i++) {
            if (i < 10 || i > 45 || i % 9 == 0 || i % 9 == 8)
                inventory.setItem(i, glassPane);
        }

        inventory.setItem(4, itemStack);

        addItems(subInventory, player);

        subInventory.addButton(new EnchantButton(exit, item -> genItemInventory(player, subInventory.getItem())), 49);
        return subInventory;
    }

    private void addItems(SubInventory subInventory, EnchantmentPlayer player) {
        ItemStack item = subInventory.getItem();

        for (EnchantmentBase enchantment : enchantmentHandler.getAllEnchants()) {
            if (!enchantment.canEnchantItem(item))
                continue;
            int level = EnchantUtils.getNextLevel(item, enchantment);
            EnchantButton button = updateItem(enchantment, level);

            assert button.getItem().getItemMeta() != null && button.getItem().getItemMeta().getLore() != null;

            if(level <= enchantment.getMaxLevel())
                button.getItem().getItemMeta().getLore().add(ChatColor.GRAY + EnchantUtils.getPriceString(player.usingGems(), level, enchantment));
            subInventory.addButton(button, subInventory.getInventory().firstEmpty());
        }
    }

    private EnchantButton updateItem(EnchantmentBase base, int level) {
        ItemStack item = EnchantmentMenuFactory.makeItem(base.getIcon(), ChatColor.GREEN + base.getName());
        addLore(level, base, item);
        assert item.getItemMeta() != null && item.getItemMeta().getLore() != null;

        if (level <= base.getMaxLevel()) {
            return new EnchantButton(item, enchantmentPlayer -> generateCallback(enchantmentPlayer, base));
        } else {
            item.setAmount(64);
            return new EnchantButton(item, enchantmentPlayer -> generateGUI(enchantmentPlayer.getCurrentGUI().getItem(), enchantmentPlayer));
        }
    }

    private EnchantmentGUI generateCallback(EnchantmentPlayer player, EnchantmentBase base) {
        ItemStack itemStack = player.getCurrentGUI().getItem();
        long price = utils.addEnchantmentBase(itemStack, base, player);
        if(price==0)
            return generateGUI(itemStack, player);
        if (!(base instanceof VanillaEnchant))
            swapLines(itemStack);
        updatePriceStr(price, itemStack);
        return generateGUI(itemStack, player);
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

    private void addLore(int level, EnchantmentBase base, ItemStack target) {
        ItemMeta meta = target.getItemMeta();
        assert meta != null;
        String levelString = TranslatedMessage.translate("enchantment.level");
        if (level <= base.getMaxLevel())
            levelString += level;
        else
            levelString += TranslatedMessage.translate("enchantment.maxed");

        List<String> lore = new ArrayList<>();
        lore.add(levelString);
        meta.setLore(lore);
        target.setItemMeta(meta);
    }

    private void updatePriceStr(long price, ItemStack item) {
        assert item.getItemMeta() != null;
        List<String> lore = item.getItemMeta().getLore();
        assert lore != null;
        String priceStr = lore.get(lore.size() - 1).replaceAll(ChatColor.COLOR_CHAR + ".", "");
        long oldPrice = Long.parseLong(priceStr.replaceAll("[^\\d.]", ""));
        long newPrice = price + oldPrice;
        priceStr = priceStr.replace("" + oldPrice, "" + newPrice);
        lore.set(lore.size() - 1, ChatColor.GRAY + priceStr);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public EnchantmentGUI genInventory(Player player) {
        return genItemInventory(handler.getPlayer(player), player.getInventory().getItemInMainHand().clone());
    }

    public EnchantmentGUI genItemInventory(EnchantmentPlayer enchantPlayer, ItemStack item) {
        Inventory inventory = Bukkit.createInventory(null, 9 * (2 + ((int) Math.ceil(buttons.size() / 7f))), "Enchantments");
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
        if (lore.isEmpty() || !lore.get(lore.size() - 1).startsWith(TranslatedMessage.translate(PRICE))) {
            if (enchantPlayer.usingGems())
                lore.add(TranslatedMessage.translate(PRICE) + "0G");
            else
                lore.add(TranslatedMessage.translate(PRICE) + " " + TranslatedMessage.translate("dollar.symbol", "0"));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        EnchantmentGUI enchantInv = new EnchantmentGUI(inventory);
        enchantInv.setOpener(enchantPlayer);
        enchantInv.setItem(item);
        populate(enchantInv, item, enchantPlayer.getPlayer());
        enchantPlayer.setCurrentGUI(null);
        enchantPlayer.getPlayer().openInventory(inventory);
        enchantPlayer.setCurrentGUI(enchantInv);
        return enchantInv;
    }

    /*
    Order:
    21: Enchant
    23: Cancel
     */
    private void populate(EnchantmentGUI inventory, ItemStack item, Player player) {
        inventory.getInventory().setItem(4, item);
        int rows = 2 + (int) Math.ceil(buttons.size() / 7f);
        for(int i = 1; i < rows-1; i++) {
            for(int i2 = 0; i2 < Math.min(7, buttons.size()-(i-1)*7); i2++) {
                inventory.addButton(buttons.get((i-1)*7+i2), i*9+i2+1);
            }
        }
        ItemStack newItem = makeItem(Material.REDSTONE_BLOCK, TranslatedMessage.translate("enchant.cancel"));
        inventory.addButton(new EnchantButton(newItem, itemStack -> null), rows * 9 - 4);

        newItem = makeItem(Material.EMERALD_BLOCK, TranslatedMessage.translate("enchant.confirm"));
        inventory.addButton(new EnchantButton(newItem, enchantmentPlayer -> {
            PlayerInventory playerInventory = player.getInventory();
            removePriceLine(enchantmentPlayer.getCurrentGUI().getItem(), handler.getPlayer(player));
            playerInventory.setItem(playerInventory.getHeldItemSlot(), enchantmentPlayer.getCurrentGUI().getItem());
            return null;
        }), rows * 9 - 6);

        int i = inventory.getInventory().firstEmpty();
        while( i != -1) {
            inventory.getInventory().setItem(i, glassPane);
            i = inventory.getInventory().firstEmpty();
        }
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

    private void generateButton(EnchantmentTarget target, Material material, String key) {
        ItemStack item = makeItem(material, TranslatedMessage.translate(key));
        buttons.add(
                new EnchantButton(item, player -> {
                    if ((target != null && target.includes(player.getCurrentGUI().getItem())) || player.getCurrentGUI().getItem().getType() == material)
                        return generateGUI(player.getCurrentGUI().getItem(), player);
                    else
                        return genItemInventory(player, player.getCurrentGUI().getItem());
                }));
    }

    public static ItemStack makeItem(Material material, String name, String... lore) {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        stack.setItemMeta(meta);
        return stack;
    }
}
