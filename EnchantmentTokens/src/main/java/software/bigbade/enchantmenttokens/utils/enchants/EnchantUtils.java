package software.bigbade.enchantmenttokens.utils.enchants;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.api.VanillaEnchant;
import software.bigbade.enchantmenttokens.listeners.gui.EnchantmentGUIListener;
import software.bigbade.enchantmenttokens.localization.TranslatedMessage;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerHandler;
import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class EnchantUtils {
    private EnchantmentHandler handler;
    private EnchantmentPlayerHandler playerHandler;
    private ListenerHandler listenerHandler;
    private Set<Location> signs;

    public EnchantUtils(EnchantmentHandler handler, EnchantmentPlayerHandler playerHandler, ListenerHandler listenerHandler, Set<Location> signs) {
        this.handler = handler;
        this.playerHandler = playerHandler;
        this.listenerHandler = listenerHandler;
        this.signs = signs;
    }

    /**
     * Adds enchantment with given name to item, removes gems, and sends messages.
     * @param itemStack item to enchant
     * @param name name of the enchantment
     * @param player who to take the gems from
     */
    public void addEnchantment(ItemStack itemStack, String name, Player player) {
        for (EnchantmentBase base : handler.getAllEnchants()) {
            if (base.getName().equals(name) && base.canEnchantItem(itemStack)) {
                EnchantmentPlayer enchantmentPlayer = playerHandler.getPlayer(player);
                enchantmentPlayer.addGems(-addEnchantmentBase(itemStack, base, enchantmentPlayer));
                return;
            }
        }
        player.sendMessage(TranslatedMessage.translate("enchantment.add.fail"));
    }

    /**
     * Adds enchantment to item
     * @param item Item to enchant
     * @param base EnchantBase to add
     * @param enchantmentPlayer player that holds the item
     * @return price of the enchant
     */
    public long addEnchantmentBase(ItemStack item, EnchantmentBase base, EnchantmentPlayer enchantmentPlayer) {
        int level = getNextLevel(item, base);
        if (level > base.getMaxLevel()) {
            enchantmentPlayer.getPlayer().sendMessage(TranslatedMessage.translate("enchantment.max.message"));
            return 0;
        }
        long price = base.getDefaultPrice(level);

        if (enchantmentPlayer.getGems() < price) {
            enchantmentPlayer.getPlayer().sendMessage(TranslatedMessage.translate("enchantment.bought.fail", getPriceString(enchantmentPlayer.usingGems(), level, base)));
            return 0;
        }
        enchantmentPlayer.getPlayer().sendMessage(TranslatedMessage.translate("enchantment.bought.success", base.getName(), "" + level));
        if (base instanceof VanillaEnchant) {
            item.addEnchantment(((VanillaEnchant) base).getEnchantment(), level);
            updateSigns(level, base, signs, enchantmentPlayer);
            return price;
        }
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.addEnchant(base, level, true);
        updateLore(meta, level, base);
        item.setItemMeta(meta);
        listenerHandler.onEnchant(item, base, enchantmentPlayer.getPlayer());
        updateSigns(level, base, signs, enchantmentPlayer);
        return price;
    }

    private void updateLore(ItemMeta meta, int level, EnchantmentBase base) {
        List<String> lore = meta.getLore();
        if (lore == null)
            lore = new ArrayList<>();
        if (level != 0)
            lore.remove(ChatColor.GRAY + base.getName() + " " + EnchantmentGUIListener.getRomanNumeral(level - 1));
        lore.add(ChatColor.GRAY + base.getName() + " " + EnchantmentGUIListener.getRomanNumeral(level));
        meta.setLore(lore);
    }

    public static int getNextLevel(ItemStack item, EnchantmentBase enchantment) {
        if(enchantment instanceof VanillaEnchant)
            return item.getEnchantmentLevel(((VanillaEnchant) enchantment).getEnchantment())+1;
        if(!item.hasItemMeta() || !Objects.requireNonNull(item.getItemMeta()).hasEnchants()) return enchantment.getStartLevel();
        int level = item.getItemMeta().getEnchants().get(enchantment);
        return level==0 ? enchantment.getStartLevel() : level+1;
    }

    public static String getPriceString(boolean gems, int level, EnchantmentBase base) {
        if (level > base.maxLevel)
            return TranslatedMessage.translate("enchantment.max");
        else
            return getPriceString(gems, base.getDefaultPrice(level));
    }

    public static String getPriceString(boolean gems, long price) {
        if (gems)
            return price + "G";
        else
            return TranslatedMessage.translate("dollar.symbol", "" + price);
    }

    private void updateSigns(int level, EnchantmentBase base, Set<Location> signs, EnchantmentPlayer player) {
        for (Location location : signs)
            if (level >= base.getMaxLevel())
                player.getPlayer().sendSignChange(location, new String[]{"[" + TranslatedMessage.translate("enchantment") + "]", base.getName(), TranslatedMessage.translate("enchantment.price.maxed"), ""});
            else
                player.getPlayer().sendSignChange(location, new String[]{"[" + TranslatedMessage.translate("enchantment") + "]", base.getName(), getPriceString(player.usingGems(), level, base), ""});
    }
}
