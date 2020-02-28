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
import software.bigbade.enchantmenttokens.api.VanillaEnchant;
import software.bigbade.enchantmenttokens.localization.TranslatedMessage;
import software.bigbade.enchantmenttokens.utils.EnchantButton;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantUtils;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;

import java.util.*;

public class EnchantmentMenuFactory implements MenuFactory {
    private ItemStack glassPane;
    private int version;
    private EnchantmentPlayerHandler handler;
    private EnchantmentHandler enchantmentHandler;
    private EnchantUtils utils;

    //Barrier used for exiting the GUI
    private ItemStack exit;

    private Map<ItemStack, EnchantButton> buttons = new LinkedHashMap<>();

    public EnchantmentMenuFactory(EnchantmentTokens main) {
        this.version = main.getVersion();
        this.handler = main.getPlayerHandler();
        this.utils = main.getUtils();
        this.enchantmentHandler = main.getEnchantmentHandler();

        exit = makeItem(Material.BARRIER, TranslatedMessage.translate("enchant.back"));
        glassPane = makeItem(Material.BLACK_STAINED_GLASS_PANE, " ");

        generateButtons();
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
     */
    private void generateButtons() {
        if (version >= 14)
            generateButton(EnchantmentTarget.CROSSBOW, Material.CROSSBOW, "tool.crossbow");
        if (version >= 13)
            generateButton(EnchantmentTarget.TRIDENT, Material.TRIDENT, "tool.trident");
        else if (version >= 9)
            generateButton(EnchantmentTarget.FISHING_ROD, Material.FISHING_ROD, "tool.fishingrod");
        generateButton(EnchantmentTarget.TOOL, Material.DIAMOND_PICKAXE, "tool.tool");
        generateButton(EnchantmentTarget.WEAPON, Material.DIAMOND_SWORD, "tool.sword");
        if (version == 13 || version < 8)
            generateButton(EnchantmentTarget.FISHING_ROD, Material.FISHING_ROD, "tool.fishingrod");
        generateButton(EnchantmentTarget.ARMOR, Material.DIAMOND_CHESTPLATE, "tool.armor");
        generateButton(EnchantmentTarget.BOW, Material.BOW, "tool.bow");
        if (version >= 14)
            generateButton(EnchantmentTarget.FISHING_ROD, Material.FISHING_ROD, "tool.fishingrod");
        else if (version >= 9)
            generateButton(null, Material.SHIELD, "tool.shield");
        if (version >= 14)
            generateButton(null, Material.SHIELD, "tool.shield");
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

        inventory.setItem(49, exit);

        addItems(subInventory, player);

        subInventory.addButton(new EnchantButton(item -> genItemInventory(player, subInventory.getItem())), 49);
        return subInventory;
    }

    private void addItems(SubInventory subInventory, EnchantmentPlayer player) {
        int next = subInventory.getInventory().firstEmpty();
        ItemStack item = subInventory.getItem();

        for (EnchantmentBase enchantment : enchantmentHandler.getAllEnchants()) {
            if (!enchantment.canEnchantItem(item))
                continue;
            EnchantButton button = updateItem(enchantment, item, player);
            subInventory.addButton(button, next);
            subInventory.getInventory().setItem(next, makeItem(enchantment.getIcon(), ChatColor.GREEN + enchantment.getName(), TranslatedMessage.translate("enchantment.price") + EnchantUtils.getPriceString(player.usingGems(), EnchantUtils.getNextLevel(subInventory.getItem(), enchantment), enchantment)));
            next = subInventory.getInventory().firstEmpty();
        }
    }

    private EnchantButton updateItem(EnchantmentBase base, ItemStack stack, EnchantmentPlayer player) {
        ItemStack item = EnchantmentMenuFactory.makeItem(base.getIcon(), ChatColor.GREEN + base.getName());
        int level = addLore(stack, base, item, player.usingGems());
        if (level <= base.getMaxLevel()) {
            return new EnchantButton(enchantmentPlayer -> {
                ItemStack itemStack = enchantmentPlayer.getCurrentGUI().getItem();
                utils.addEnchantmentBase(itemStack, base, player.getPlayer());
                if (!(base instanceof VanillaEnchant))
                    swapLines(itemStack);
                updatePriceStr(base, level, itemStack);
                return generateGUI(itemStack, player);
            });
        } else {
            item.setAmount(64);
            return new EnchantButton(enchantmentPlayer -> generateGUI(enchantmentPlayer.getCurrentGUI().getItem(), enchantmentPlayer));
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

    private int addLore(ItemStack item, EnchantmentBase base, ItemStack target, boolean gems) {
        int level = EnchantUtils.getNextLevel(item, base);
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
        long price = Long.parseLong(priceStr.replaceAll(ChatColor.COLOR_CHAR + ".", "").replaceAll("[^\\d.]", ""));
        long newPrice = price + base.getDefaultPrice(level);
        priceStr = priceStr.replace("" + price, "" + newPrice);
        lore.set(lore.size() - 1, priceStr);
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
        if (lore.isEmpty() || !lore.get(lore.size() - 1).startsWith(TranslatedMessage.translate("enchantment.price"))) {
            if (enchantPlayer.usingGems())
                lore.add(TranslatedMessage.translate("enchantment.price") + "0G");
            else
                lore.add(TranslatedMessage.translate("enchantment.price") + " " + TranslatedMessage.translate("dollar.symbol", "0"));
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
        //TODO refine this logic.
        int rows = 2 + (int) Math.ceil(buttons.size() / 7f);
        ItemStack[] items = buttons.keySet().toArray(new ItemStack[]{});
        for(int i = 1; i < rows-1; i++) {
            for(int i2 = 1; i2 < Math.min(8, items.length-(i-1)*9); i2++) {
                ItemStack buttonItem = items[i2+(i-1)*9];
                inventory.getInventory().setItem(i*9+i2, buttonItem);
                inventory.addButton(buttons.get(buttonItem), i*9+i2);
            }
        }
        ItemStack newItem = makeItem(Material.REDSTONE_BLOCK, TranslatedMessage.translate("enchant.cancel"));
        inventory.addButton(new EnchantButton(itemStack -> null), rows * 9 - 4);
        inventory.getInventory().setItem(rows * 9 - 4, newItem);

        newItem = makeItem(Material.EMERALD_BLOCK, TranslatedMessage.translate("enchant.confirm"));
        inventory.addButton(new EnchantButton(enchantmentPlayer -> {
            PlayerInventory playerInventory = player.getInventory();
            removePriceLine(enchantmentPlayer.getCurrentGUI().getItem(), handler.getPlayer(player));
            playerInventory.setItem(playerInventory.getHeldItemSlot(), enchantmentPlayer.getCurrentGUI().getItem());
            return null;
        }), rows * 9 - 6);

        int i = inventory.getInventory().firstEmpty();
        while( i != -1) {
            inventory.getInventory().addItem(glassPane);
            i = inventory.getInventory().firstEmpty();
        }
        inventory.getInventory().setItem(rows * 9 - 6, newItem);
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
        buttons.put(item,
                new EnchantButton(player -> {
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
