package software.bigbade.enchantmenttokens.utils.enchants;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.api.VanillaEnchant;
import software.bigbade.enchantmenttokens.localization.TranslatedPrice;
import software.bigbade.enchantmenttokens.utils.RomanNumeralConverter;
import software.bigbade.enchantmenttokens.utils.listeners.EnchantListenerHandler;
import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class CustomEnchantUtils implements EnchantUtils {
    private EnchantmentHandler handler;
    private EnchantmentPlayerHandler playerHandler;
    private EnchantListenerHandler listenerHandler;
    private Set<Location> signs;

    public CustomEnchantUtils(EnchantmentHandler handler, EnchantmentPlayerHandler playerHandler, EnchantListenerHandler listenerHandler, Set<Location> signs) {
        this.handler = handler;
        this.playerHandler = playerHandler;
        this.listenerHandler = listenerHandler;
        this.signs = signs;
    }

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
            item.addEnchantment(base.getEnchantment(), level);
            updateSigns(level, base, signs, enchantmentPlayer);
            return price;
        }
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.addEnchant(base.getEnchantment(), level, true);
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
            lore.remove(ChatColor.GRAY + base.getName() + " " + RomanNumeralConverter.getRomanNumeral(level - 1));
        lore.add(ChatColor.GRAY + base.getName() + " " + RomanNumeralConverter.getRomanNumeral(level));
        meta.setLore(lore);
    }

    public static int getNextLevel(ItemStack item, EnchantmentBase enchantment) {
        if(enchantment instanceof VanillaEnchant)
            return item.getEnchantmentLevel(enchantment.getEnchantment())+1;
        if(!item.hasItemMeta() || !Objects.requireNonNull(item.getItemMeta()).hasEnchants()) return enchantment.getStartLevel();
        int level = item.getItemMeta().getEnchants().get(enchantment.getEnchantment());
        return level==0 ? enchantment.getStartLevel() : level+1;
    }

    public static String getPriceString(int level, EnchantmentBase base) {
        if (level > base.getMaxLevel())
            return TranslatedMessage.translate("enchantment.max");
        else
            return new TranslatedPrice().translate("" + base.getDefaultPrice(level));
    }

    private void updateSigns(int level, EnchantmentBase base, Set<Location> signs, EnchantmentPlayer player) {
        for (Location location : signs)
            if (level >= base.getMaxLevel())
                player.getPlayer().sendSignChange(location, new String[]{"[" + TranslatedMessage.translate("enchantment") + "]", base.getName(), TranslatedMessage.translate("enchantment.price.maxed"), ""});
            else
                player.getPlayer().sendSignChange(location, new String[]{"[" + TranslatedMessage.translate("enchantment") + "]", base.getName(), getPriceString(player.usingGems(), level, base), ""});
    }
}
