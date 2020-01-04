package bigbade.enchantmenttokens.gui;

import bigbade.enchantmenttokens.EnchantmentTokens;
import bigbade.enchantmenttokens.api.EnchantmentBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Map;

public class EnchantGUI {
    private EnchantmentTokens main;
    private ConfigurationSection section;

    private ItemStack[] vanillaIcons = new ItemStack[]{};
    private ItemStack greyPlane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
    private ItemStack exit;

    public EnchantGUI(EnchantmentTokens main) {
        this.main = main;
        section = main.getConfig().getConfigurationSection("enchants");
        exit = new ItemStack(Material.BARRIER);
        ItemMeta meta = exit.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Back to enchantments");
        exit.setItemMeta(meta);
    }

    public Inventory generateGUI(EnchantmentTarget target, ItemStack itemStack, String name) {
        Inventory inventory = Bukkit.createInventory(null, 54, "Enchantments for " + name);

        for (int i = 0; i < 9; i++)
            inventory.setItem(i, greyPlane);
        for (int i = 8; i < 45; i++) {
            inventory.setItem(i, greyPlane);
            if (i % 9 == 0) {
                i += 7;
            }
        }
        for (int i = 45; i < 54; i++) {
            inventory.setItem(i, greyPlane);
        }

        inventory.setItem(4, itemStack);
        int i = 0;

        for (Enchantment enchantment : main.vanillaEnchants) {
            if (enchantment.getItemTarget() == target || enchantment.getItemTarget() == EnchantmentTarget.ALL) {
                ItemStack item = new ItemStack(vanillaIcons[i]);
                for (Map.Entry<Enchantment, Integer> enchantment1 : itemStack.getEnchantments().entrySet()) {
                    if (enchantment1.getKey().getKey().equals(enchantment.getKey())) {
                        item.setAmount(enchantment1.getValue() + 1);
                        if (item.getAmount() > enchantment.getMaxLevel())
                            item.setAmount(0);
                        break;
                    }
                }
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.GREEN + enchantment.getName());
                meta.setLore(Arrays.asList(ChatColor.GRAY + "Price: " + section.getConfigurationSection(enchantment.getName()).getConfigurationSection("prices").getLong("" + item.getAmount()), ChatColor.GRAY + "Level: " + item.getAmount()));
                item.setItemMeta(meta);
                inventory.addItem(item);
            }
            i += 1;
        }
        for (EnchantmentBase enchantment : main.enchantments) {
            if (enchantment.getItemTarget() == target || enchantment.getItemTarget() == EnchantmentTarget.ALL) {
                ItemStack item = new ItemStack(enchantment.getIcon());
                for (Map.Entry<Enchantment, Integer> enchantment1 : itemStack.getEnchantments().entrySet()) {
                    if (enchantment1.getKey().getKey().equals(enchantment.getKey())) {
                        item.setAmount(enchantment1.getValue());
                        if (item.getAmount() > enchantment.getMaxLevel())
                            item.setAmount(0);
                        break;
                    }
                }
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(enchantment.getName());
                //Here
                if (item.getAmount() > 0) {
                    meta.setLore(Arrays.asList(ChatColor.GRAY + "Price: " + enchantment.getDefaultPrice(item.getAmount()), ChatColor.GRAY + "Level: " + item.getAmount()));
                } else
                    meta.setLore(Arrays.asList(ChatColor.GRAY + "MAXED"));
                //To here
                item.setItemMeta(meta);
                inventory.addItem(item);

            }
            i += 1;
        }
        inventory.setItem(49, exit);
        return inventory;
    }
}
