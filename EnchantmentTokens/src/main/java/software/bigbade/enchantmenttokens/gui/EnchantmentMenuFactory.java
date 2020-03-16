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
import software.bigbade.enchantmenttokens.localization.TranslatedTextMessage;
import software.bigbade.enchantmenttokens.utils.EnchantButton;
import software.bigbade.enchantmenttokens.utils.currency.CurrencyAdditionHandler;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantUtils;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnchantmentMenuFactory implements MenuFactory {
    private ItemStack glassPane = makeItem(Material.BLACK_STAINED_GLASS_PANE, " ");
    private int version;
    private EnchantmentPlayerHandler handler;
    private EnchantmentHandler enchantmentHandler;
    private EnchantUtils utils;

    private static final TranslatedTextMessage PRICE = new TranslatedTextMessage("enchantment.price");

    private static final String BACK = new TranslatedTextMessage("enchant.back").getText();
    //Barrier used for exiting the GUI
    private ItemStack exit = makeItem(Material.BARRIER, BACK);
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

    private static final TranslatedTextMessage TOOLENCHANTS = new TranslatedTextMessage("tool.enchants");
    /**
     * Generate the GUI with every enchantment in it
     *
     * @param itemStack Item target
     * @param player    Target player
     * @return Generated enchantment inventory
     */
    public SubInventory generateGUI(ItemStack itemStack, EnchantmentPlayer player) {
        Inventory inventory = Bukkit.createInventory(null, 54, TOOLENCHANTS.getText(VanillaEnchant.capitalizeString(itemStack.getType().name().replace("_", " ").toLowerCase())));
        SubInventory subInventory = new SubInventory(inventory);

        subInventory.setItem(itemStack);

        for (int i = 0; i < 54; i++) {
            if (i < 10 || i > 45 || i % 9 == 0 || i % 9 == 8)
                inventory.setItem(i, glassPane);
        }

        inventory.setItem(4, itemStack);

        addItems(subInventory);

        player.setCurrentGUI(subInventory);

        subInventory.addButton(new EnchantButton(exit, item -> genItemInventory(player, subInventory.getItem())), 49);
        return subInventory;
    }

    private void addItems(SubInventory subInventory) {
        ItemStack item = subInventory.getItem();

        enchantmentHandler.getAllEnchants().stream()
                .filter(base -> base.canEnchantItem(item))
                .forEach(base -> {
                    int level = EnchantUtils.getNextLevel(item, base);
                    EnchantButton button = updateItem(base, level);

                    assert button.getItem().getItemMeta() != null && button.getItem().getItemMeta().getLore() != null;

                    if(level <= base.getMaxLevel())
                        button.getItem().getItemMeta().getLore().add(ChatColor.GRAY + EnchantUtils.getPriceString(level, base));
                    subInventory.addButton(button, subInventory.getInventory().firstEmpty());
                });
    }

    private EnchantButton updateItem(EnchantmentBase base, int level) {
        ItemStack item = EnchantmentMenuFactory.makeItem(base.getIcon(), ChatColor.GREEN + base.getName());
        addLevelLore(level, base.getMaxLevel(), item);
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

    private static final TranslatedTextMessage LEVEL = new TranslatedTextMessage("enchantment.level");
    private static final String MAXED = new TranslatedTextMessage("enchantment.maxed").getText();

    private void addLevelLore(int level, int maxLevel, ItemStack target) {
        ItemMeta meta = target.getItemMeta();
        assert meta != null;
        String levelString;
        if (level <= maxLevel)
            levelString = LEVEL.getText(level + "");
        else
            levelString = MAXED;
        List<String> lore = meta.getLore();
        if(lore == null)
            lore = new ArrayList<>();
        lore.add(levelString);
        meta.setLore(lore);
        target.setItemMeta(meta);
    }

    private void updatePriceStr(long price, ItemStack item) {
        assert item.getItemMeta() != null;
        List<String> lore = item.getItemMeta().getLore();
        assert lore != null;
        String priceStr = ChatColor.stripColor(lore.get(lore.size() - 1));
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
        Inventory inventory = Bukkit.createInventory(null, 9 * (2 + ((int) Math.ceil(buttons.size() / 7f))), TOOLENCHANTS.getText(VanillaEnchant.capitalizeString(item.getType().name().replace("_", " ").toLowerCase())));
        if(item.getType() == Material.AIR)
            return null;
        setupEncantItem(item);
        EnchantmentGUI enchantInv = new CustomEnchantmentGUI(inventory);
        enchantInv.setItem(item);
        populate(enchantInv, item, enchantPlayer.getPlayer());
        enchantPlayer.setCurrentGUI(null);
        enchantPlayer.getPlayer().openInventory(inventory);
        enchantPlayer.setCurrentGUI(enchantInv);
        return enchantInv;
    }

    private void setupEncantItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        addPrice(meta);
        item.setItemMeta(meta);
    }

    private void addPrice(ItemMeta meta) {
        List<String> lore = meta.getLore();
        if (lore == null)
            lore = new ArrayList<>();
        if (lore.isEmpty() || !PRICE.getText("").startsWith(lore.get(lore.size() - 1))) {
            lore.add(PRICE.getText(CurrencyAdditionHandler.getInstance().formatMoney(0)));
        }
        meta.setLore(lore);
    }

    private static final String CONFIRM = new TranslatedTextMessage("enchant.confirm").getText();

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
        ItemStack newItem = makeItem(Material.REDSTONE_BLOCK, BACK);
        inventory.addButton(new EnchantButton(newItem, itemStack -> null), rows * 9 - 4);

        newItem = makeItem(Material.EMERALD_BLOCK, CONFIRM);
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
            long price = Long.parseLong(line.substring(9).replace(CurrencyAdditionHandler.getInstance().getFormat(), ""));
            List<String> lore = meta.getLore();
            lore.remove(meta.getLore().size() - 1);
            meta.setLore(lore);

            item.setItemMeta(meta);
            player.addGems(-price);
            player.getPlayer().getInventory().setItemInMainHand(item);
        }
    }

    private void generateButton(EnchantmentTarget target, Material material, String key) {
        ItemStack item = makeItem(material, new TranslatedTextMessage(key).getText());
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
