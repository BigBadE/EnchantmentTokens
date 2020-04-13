/*
 * Addons for the Custom Enchantment API in Minecraft
 * Copyright (C) 2020 BigBadE
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package software.bigbade.enchantmenttokens.utils.enchants;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;

public abstract class EnchantUtils {

    private static EnchantUtils instance;

    /**
     * Adds enchantment with given name to item, removes gems, and sends messages.
     * @param itemStack item to enchant
     * @param name name of the enchantment
     * @param player who to take the gems from
     */
    public abstract void addEnchantment(ItemStack itemStack, String name, Player player);

    /**
     * Adds enchantment to item.
     * @param item Item to enchant
     * @param base EnchantBase to add
     * @param enchantmentPlayer player that holds the item
     * @return price of the enchant
     */
    public abstract long addEnchantmentBase(ItemStack item, EnchantmentBase base, EnchantmentPlayer enchantmentPlayer);

    public abstract int getNextLevel(ItemStack item, EnchantmentBase enchantment);

    static void setInstance(EnchantUtils instance)  {
        if(instance == null)
            throw new IllegalStateException("Instance already set!");
        EnchantUtils.instance = instance;
    }

    public static EnchantUtils getInstance() {
        return EnchantUtils.instance;
    }
}
