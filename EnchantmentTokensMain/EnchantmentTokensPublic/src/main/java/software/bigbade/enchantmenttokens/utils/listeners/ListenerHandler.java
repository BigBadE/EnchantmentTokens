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

package software.bigbade.enchantmenttokens.utils.listeners;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.api.EnchantmentAddon;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.ListenerType;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface ListenerHandler {
    void registerListeners();

    void loadAddons(Collection<EnchantmentAddon> addons);

    void onEnchant(ItemStack item, EnchantmentBase base, Player player);

    void loadEnchantments(Map<EnchantmentAddon, Set<Class<EnchantmentBase>>> enchants);

    ListenerManager getListenerManager(ListenerType type);
}
