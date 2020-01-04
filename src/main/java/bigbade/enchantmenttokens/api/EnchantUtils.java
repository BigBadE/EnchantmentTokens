package bigbade.enchantmenttokens.api;

import bigbade.enchantmenttokens.EnchantmentTokens;
import bigbade.enchantmenttokens.events.EnchantmentApplyEvent;
import bigbade.enchantmenttokens.listeners.gui.EnchantmentGUIListener;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EnchantUtils {
    public static void addEnchantment(ItemStack itemStack, String name, EnchantmentTokens main, Player player, ConfigurationSection section, boolean key) {
        for (EnchantmentBase base : main.enchantments) {
            boolean correct = false;
            if (base.getName().equals(name)) {
                correct = true;
            }
            if (correct) {
                if (base.canEnchantItem(itemStack)) {
                    long price;
                    int level = base.getStartLevel();
                    for (Map.Entry<Enchantment, Integer> enchants : itemStack.getEnchantments().entrySet()) {
                        if (enchants.getKey().getKey().equals(base.getKey())) {
                            level = enchants.getValue();
                            break;
                        }
                    }
                    if (level > base.getMaxLevel()) {
                        player.sendMessage(ChatColor.RED + "You already have the max level enchantment!");
                        return;
                    }
                    price = base.getDefaultPrice(level);
                    EnchantmentPlayer player1 = main.fileLoader.loadPlayer(player);
                    if (player1.getGems() >= price) {
                        if (key)
                            player1.addGems(-price);
                        player.sendMessage(ChatColor.GREEN + "Successfully bought " + base.getName() + " level " + level + ".");
                        ItemMeta meta = itemStack.getItemMeta();
                        meta.addEnchant(base, level+1, true);
                        List<String> lore;
                        lore = meta.getLore();
                        if (lore == null)
                            lore = new ArrayList<>();
                        String temp = null;
                        if (!key) {
                            if (lore.get(lore.size() - 1).substring(0, 9).equals(ChatColor.GRAY + "Price: ")) {
                                temp = lore.get(lore.size() - 1);
                                lore.remove(temp);
                            }
                        }
                        if (level != 0)
                            lore.remove(ChatColor.GRAY + base.getName() + " " + EnchantmentGUIListener.getRomanNumeral(level-1));
                        lore.add(ChatColor.GRAY + base.getName() + " " + EnchantmentGUIListener.getRomanNumeral(level));
                        if (temp != null) {
                            int currentPrice = Integer.parseInt(temp.substring(9, temp.length()-1));
                            temp = ChatColor.GRAY + "Price: " + (currentPrice+base.getDefaultPrice(level)) + "G";
                            lore.add(temp);
                        }
                        meta.setLore(lore);
                        itemStack.setItemMeta(meta);
                        for (Location location : main.signs)
                            if (level >= base.getMaxLevel())
                                player.sendSignChange(location, new String[]{"[Enchantment]", base.getName(), "Price: Maxed!", ""});
                            else
                                player.sendSignChange(location, new String[]{"[Enchantment]", base.getName(), "Price: " + base.getDefaultPrice(level) + "G", ""});
                    } else
                        player.sendMessage(ChatColor.RED + "You do not have " + price + "G!");
                    for (Map.Entry<EnchantmentBase, Consumer<Event>> enchantment : main.getListeners(ListenerType.ENCHANT).entrySet()) {
                        if(enchantment.getKey().equals(base)) {
                            enchantment.getValue().accept(new EnchantmentApplyEvent(itemStack, player));
                        }
                    }
                    return;
                }
            }
        }
        for (VanillaEnchant base : main.vanillaEnchants) {
            boolean correct = false;
            if (base.getName().equals(name))
                correct = true;
            if (correct)
                if (base.canEnchantItem(itemStack)) {
                    long price;
                    int level = 1;
                    for (Map.Entry<Enchantment, Integer> enchats : itemStack.getEnchantments().entrySet())
                        if (enchats.getKey().getKey().equals(base.getKey())) {
                            level = enchats.getValue() + 1;
                            break;
                        }
                    if (level > base.getMaxLevel()) {
                        player.sendMessage(ChatColor.RED + "You already have the max level enchantment!");
                        return;
                    }
                    price = section.getConfigurationSection(base.getKey().getKey()).getConfigurationSection("prices").getLong(level + "");
                    EnchantmentPlayer player1 = main.fileLoader.loadPlayer(player);
                    if (player1.getGems() >= price) {
                        player1.addGems(-price);
                        player.sendMessage(ChatColor.GREEN + "Successfully bought " + base.getName() + " level " + level + ".");
                        itemStack.addEnchantment(base, level);
                        ItemMeta meta = itemStack.getItemMeta();
                        meta.getLore().add(base.getName() + ": " + EnchantmentGUIListener.getRomanNumeral(level));
                        for (Location location : main.signs)
                            player.sendSignChange(location, new String[]{"[Enchantment]", base.getName(), "", ""});
                    } else {
                        player.sendMessage(ChatColor.RED + "You do not have " + price + "G!");
                    }
                    return;
                }
        }
        player.sendMessage(ChatColor.RED + "Could not find that enchantment!");
    }
}
