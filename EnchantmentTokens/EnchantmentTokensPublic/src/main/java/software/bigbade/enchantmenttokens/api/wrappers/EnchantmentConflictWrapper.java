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

package software.bigbade.enchantmenttokens.api.wrappers;

import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantmentConflictWrapper implements IConflictWrapper {
    private Map<String, List<String>> conflicts = new HashMap<>();

    @Override
    public boolean conflicts(Enchantment enchantment) {
        return false;
    }

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    @Override
    public void addTarget(String addon, String name) {
        if (conflicts.containsKey(addon))
            conflicts.get(addon).add(name);
        else
            conflicts.put(addon, Arrays.asList(name));
    }

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    @Override
    public void addTarget(Enchantment enchantment) {
        if (conflicts.containsKey(enchantment.getKey().getNamespace()))
            conflicts.get(enchantment.getKey().getNamespace()).add(enchantment.getKey().getKey());
        else
            conflicts.put(enchantment.getKey().getNamespace(), Arrays.asList(enchantment.getKey().getKey()));
    }
}
