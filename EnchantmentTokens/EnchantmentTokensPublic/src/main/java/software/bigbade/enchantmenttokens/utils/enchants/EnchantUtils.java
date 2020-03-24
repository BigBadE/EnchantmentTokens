package software.bigbade.enchantmenttokens.utils.enchants;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;

public interface EnchantUtils {

    /**
     * Adds enchantment with given name to item, removes gems, and sends messages.
     * @param itemStack item to enchant
     * @param name name of the enchantment
     * @param player who to take the gems from
     */
    void addEnchantment(ItemStack itemStack, String name, Player player);

    /**
     * Adds enchantment to item
     * @param item Item to enchant
     * @param base EnchantBase to add
     * @param enchantmentPlayer player that holds the item
     * @return price of the enchant
     */
    long addEnchantmentBase(ItemStack item, EnchantmentBase base, EnchantmentPlayer enchantmentPlayer);


}
