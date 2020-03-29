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
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.api.VanillaEnchant;
import software.bigbade.enchantmenttokens.localization.TranslatedPrice;
import software.bigbade.enchantmenttokens.utils.CustomEnchantButton;
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

    //Barrier used for exiting the GUI
    private ItemStack exit = makeItem(Material.BARRIER, StringUtils.GUI_BACK);
    private EnchantButton glassButton = new CustomEnchantButton(glassPane, player -> genItemInventory(player, player.getCurrentGUI().getItem()));

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
            generateButton(EnchantmentTarget.CROSSBOW, Material.CROSSBOW, StringUtils.TOOL_CROSSBOW);
        if (version >= 13)
            generateButton(EnchantmentTarget.TRIDENT, Material.TRIDENT, StringUtils.TOOL_TRIDENT);
        else if (version >= 9)
            generateButton(EnchantmentTarget.FISHING_ROD, Material.FISHING_ROD, StringUtils.TOOL_FISHING_ROD);
        generateButton(EnchantmentTarget.TOOL, Material.DIAMOND_PICKAXE, StringUtils.TOOL_TOOLS);
        generateButton(EnchantmentTarget.WEAPON, Material.DIAMOND_SWORD, StringUtils.TOOL_SWORD);
        if (version == 13 || version < 8)
            generateButton(EnchantmentTarget.FISHING_ROD, Material.FISHING_ROD, StringUtils.TOOL_FISHING_ROD);
        generateButton(EnchantmentTarget.ARMOR, Material.DIAMOND_CHESTPLATE, StringUtils.TOOL_ARMOR);
        generateButton(EnchantmentTarget.BOW, Material.BOW, StringUtils.TOOL_BOW);
        if (version >= 14)
            generateButton(EnchantmentTarget.FISHING_ROD, Material.FISHING_ROD, StringUtils.TOOL_FISHING_ROD);
        else if (version >= 9)
            generateButton(null, Material.SHIELD, StringUtils.TOOL_SHIELD);
        if (version >= 14) {
            addEmptyItems(3);
            generateButton(null, Material.SHIELD, StringUtils.TOOL_SHIELD);
            addEmptyItems(3);
        }
    }

    private void addEmptyItems(int amount) {
        for (int i = 0; i < amount; i++)
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
        Inventory inventory = Bukkit.createInventory(null, 54, getName(itemStack));
        SubInventory subInventory = new SubInventory(inventory);

        subInventory.setOpener(player);
        subInventory.setItem(itemStack);

        for (int i = 0; i < 54; i++) {
            if (i < 10 || i > 45 || i % 9 == 0 || i % 9 == 8)
                inventory.setItem(i, glassPane);
        }

        inventory.setItem(4, itemStack);

        addItems(subInventory, player);

        subInventory.addButton(new CustomEnchantButton(exit, item -> genItemInventory(player, subInventory.getItem())), 49);
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
            next = subInventory.getInventory().firstEmpty();
        }
    }

    private EnchantButton updateItem(EnchantmentBase base, ItemStack stack, EnchantmentPlayer player) {
        ItemStack item = EnchantmentMenuFactory.makeItem(base.getIcon(), ChatColor.GREEN + base.getEnchantmentName());
        int level = utils.getNextLevel(stack, base);
        addLore(base, item, level);
        if (level <= base.getMaxLevel()) {
            return new CustomEnchantButton(item, enchantmentPlayer -> {
                ItemStack itemStack = enchantmentPlayer.getCurrentGUI().getItem();
                long price = utils.addEnchantmentBase(itemStack, base, player);
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

    private void addLore(EnchantmentBase base, ItemStack target, int level) {
        ItemMeta meta = target.getItemMeta();
        assert meta != null;
        String priceStr = ChatColor.GRAY + StringUtils.PRICE.translate(new TranslatedPrice().translate("" + base.getDefaultPrice(level)));
        String levelStr;
        if (level <= base.getMaxLevel())
            levelStr = StringUtils.LEVEL.translate("" + level);
        else
            levelStr = StringUtils.MAXED;

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

    private String getName(ItemStack item) {
        return StringUtils.TOOL_ENCHANTS.translate(VanillaEnchant.capitalizeString(item.getType().name().toLowerCase().replace("_", " ")));
    }

    public EnchantmentGUI genItemInventory(EnchantmentPlayer enchantPlayer, ItemStack item) {
        Inventory inventory = Bukkit.createInventory(null, 9 * (2 + ((int) Math.ceil(buttons.size() / 7f))), getName(item));
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
        addPriceLine(lore);

        meta.setLore(lore);
        item.setItemMeta(meta);
        EnchantmentGUI enchantInv = new CustomEnchantmentGUI(inventory);
        enchantInv.setOpener(enchantPlayer);
        enchantInv.setItem(item);
        populate(enchantInv, item, enchantPlayer.getPlayer());
        enchantPlayer.setCurrentGUI(null);
        enchantPlayer.getPlayer().openInventory(inventory);
        enchantPlayer.setCurrentGUI(enchantInv);
        return enchantInv;
    }

    private void addPriceLine(List<String> lore) {
        if (lore.isEmpty()) {
            lore.add(StringUtils.PRICE.translate(new TranslatedPrice().translate("0")));
        } else {
            if (!lore.get(lore.size() - 1).startsWith(StringUtils.PRICE.translate("")))
                lore.add(StringUtils.PRICE.translate(new TranslatedPrice().translate("0")));
        }
    }

    /*
    Order:
    21: Enchant
    23: Cancel
     */
    private void populate(EnchantmentGUI inventory, ItemStack item, Player player) {
        inventory.getInventory().setItem(4, item);
        int rows = 2 + (int) Math.ceil(buttons.size() / 7f);
        for (int i = 1; i < rows - 1; i++) {
            for (int i2 = 0; i2 < Math.min(7, buttons.size() - (i - 1) * 7); i2++) {
                inventory.addButton(buttons.get((i - 1) * 7 + i2), i * 9 + i2 + 1);
            }
        }
        ItemStack newItem = makeItem(Material.REDSTONE_BLOCK, StringUtils.GUI_CANCEL);
        inventory.addButton(new CustomEnchantButton(newItem, itemStack -> null), rows * 9 - 4);

        newItem = makeItem(Material.EMERALD_BLOCK, StringUtils.GUI_CONFIRM);
        inventory.addButton(new CustomEnchantButton(newItem, enchantmentPlayer -> {
            PlayerInventory playerInventory = player.getInventory();
            removePriceLine(enchantmentPlayer.getCurrentGUI().getItem(), handler.getPlayer(player));
            playerInventory.setItem(playerInventory.getHeldItemSlot(), enchantmentPlayer.getCurrentGUI().getItem());
            return null;
        }), rows * 9 - 6);

        int i = inventory.getInventory().firstEmpty();
        while (i != -1) {
            inventory.getInventory().setItem(i, glassPane);
            i = inventory.getInventory().firstEmpty();
        }
    }

    private void removePriceLine(ItemStack item, EnchantmentPlayer player) {
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        if (meta.getLore() != null) {
            String line = meta.getLore().get(meta.getLore().size() - 1);
            long price = Long.parseLong(line.replaceAll("[^\\d.]", ""));
            List<String> lore = meta.getLore();
            lore.remove(meta.getLore().size() - 1);
            meta.setLore(lore);

            item.setItemMeta(meta);
            player.addGems(-price);
            player.getPlayer().getInventory().setItemInMainHand(item);
        }
    }

    private void generateButton(EnchantmentTarget target, Material material, String name) {
        ItemStack item = makeItem(material, name);
        buttons.add(
                new CustomEnchantButton(item, player -> {
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
