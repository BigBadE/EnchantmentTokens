package bigbade.enchantmenttokens.gui;

import bigbade.enchantmenttokens.api.EnchantmentPlayer;
import bigbade.enchantmenttokens.localization.TranslatedMessage;
import bigbade.enchantmenttokens.utils.EnchantButton;
import bigbade.enchantmenttokens.utils.EnchantmentPlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentMenuFactory {
    private static ItemStack glassPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
    private int version;
    private EnchantmentPlayerHandler handler;
    private EnchantmentPickerManager manager;

    public EnchantmentMenuFactory(int version, EnchantmentPlayerHandler handler, EnchantmentPickerManager manager) {
        this.version = version;
        this.handler = handler;
        this.manager = manager;
    }

    public EnchantmentGUI genInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, "Enchantments");
        ItemStack item = player.getInventory().getItemInMainHand().clone();
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            if (item.getType() == Material.AIR)
                return null;
            else
                meta = Bukkit.getItemFactory().getItemMeta(item.getType());
        assert meta != null;
        List<String> lore = meta.getLore();
        if (lore == null)
            lore = new ArrayList<>();
        EnchantmentPlayer enchantPlayer = handler.getPlayer(player);
        if (enchantPlayer.usingGems())
            lore.add(TranslatedMessage.translate("enchantment.price") + "0G");
        else
            lore.add(TranslatedMessage.translate("enchantment.price") + " " + TranslatedMessage.translate("dollar.symbol", "0"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        EnchantmentGUI enchantInv = new EnchantmentGUI(inventory);
        populate(enchantInv, item, player);
        int i = inventory.firstEmpty();
        while (i > -1 && i < 28) {
            inventory.setItem(i, glassPane);
            i = inventory.firstEmpty();
        }
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
            generateButton(player, inventory, Material.CROSSBOW, "tool.crossbow", EnchantmentTarget.CROSSBOW, 9);
        if(version >= 13)
            generateButton(player, inventory, Material.TRIDENT, "tool.trident", EnchantmentTarget.TRIDENT, 10);
        else if(version >= 9)
            generateButton(player, inventory, Material.FISHING_ROD, "tool.fishingrod", EnchantmentTarget.FISHING_ROD, 10);
        generateButton(player, inventory, Material.DIAMOND_PICKAXE, "tool.tool", EnchantmentTarget.TOOL, 11);
        generateButton(player, inventory, Material.DIAMOND_SWORD, "tool.sword", EnchantmentTarget.WEAPON, 12);
        if(version >= 13 || version < 8)
            generateButton(player, inventory, Material.FISHING_ROD, "tool.fishingrod", EnchantmentTarget.FISHING_ROD, 13);
        generateButton(player, inventory, Material.DIAMOND_CHESTPLATE, "tool.armor", EnchantmentTarget.ARMOR, 14);
        generateButton(player, inventory, Material.BOW, "tool.bow", EnchantmentTarget.BOW, 15);
        if(version >= 14)
            generateButton(player, inventory, Material.FISHING_ROD, "tool.fishingrod", EnchantmentTarget.FISHING_ROD, 16);
        else if(version >= 9)
            generateButton(player, inventory, Material.SHIELD, "tool.shield", null, 16);
        if(version >= 14)
            generateButton(player, inventory, Material.SHIELD, "tool.shield", null, 17);
        ItemStack newItem = makeItem(Material.REDSTONE_BLOCK, TranslatedMessage.translate("enchant.cancel"));
        inventory.addButton(new EnchantButton(newItem, (itemStack) -> null));
        inventory.getInventory().setItem(21, newItem);

        newItem = makeItem(Material.EMERALD_BLOCK, TranslatedMessage.translate("enchant.confirm"));
        inventory.addButton(new EnchantButton(newItem, (itemStack) -> {
            PlayerInventory playerInventory = player.getInventory();
            removePriceLine(itemStack, handler.getPlayer(player));
            playerInventory.setItem(playerInventory.getHeldItemSlot(), itemStack);
            return null;
        }));
        inventory.getInventory().setItem(23, newItem);
    }

    private void removePriceLine(ItemStack item, EnchantmentPlayer player) {
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        if (meta.getLore() != null) {
            String line = meta.getLore().get(meta.getLore().size() - 1);
            long price = Long.parseLong(line.substring(9).replace("G", ""));
            List<String> lore = meta.getLore();
            lore.remove(meta.getLore().size() - 1);
            meta.setLore(lore);

            item.setItemMeta(meta);
            player.addGems(-price);
            player.getPlayer().getInventory().setItemInMainHand(item);
        }
    }

    private void generateButton(Player player, EnchantmentGUI inventory, Material material, String key, EnchantmentTarget target, int slot) {
        ItemStack item = makeItem(material, TranslatedMessage.translate(key));
        inventory.addButton(new EnchantButton(item, (itemStack) -> manager.generateGUI(target, itemStack, player, TranslatedMessage.translate(key))));
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
