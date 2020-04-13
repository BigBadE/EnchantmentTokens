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

import software.bigbade.enchantmenttokens.api.EnchantmentBase;

import java.util.Collection;
import java.util.List;

public interface EnchantmentHandler {
    void registerEnchants(Collection<EnchantmentBase> enchantments);

    void unregisterEnchants();

    /**
     * Returns all enchantments used
     * @return All registered enchantments, including vanilla and skript enchantments
     */
    List<EnchantmentBase> getAllEnchants();

    /**
     * Gets custom enchantments
     * @return All registered custom enchantments, excluding vanilla or skript enchantments
     */
    List<EnchantmentBase> getCustomEnchants();

    /**
     * Gets Skript enchantments
     * @return All registered skript enchantments
     */
    List<EnchantmentBase> getSkriptEnchant();

    /**
     * Add Skript enchantment
     */
    void addSkriptEnchant(EnchantmentBase base);
}
