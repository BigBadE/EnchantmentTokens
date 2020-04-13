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

import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentTarget;
import software.bigbade.enchantmenttokens.EnchantmentTokens;

import java.util.List;
import java.util.logging.Level;

public class EnchantmentTargetWrapper implements ITargetWrapper {
    private final EnchantmentTarget[] targets;

    public EnchantmentTargetWrapper(EnchantmentTarget... targets) {
        this.targets = targets;
    }

    public EnchantmentTargetWrapper(String... targets) {
        this.targets = new EnchantmentTarget[targets.length];
        for (int i = 0; i < targets.length; i++)
            try {
                this.targets[i] = EnchantmentTarget.valueOf(targets[i]);
            } catch (IllegalArgumentException e) {
                EnchantmentTokens.getEnchantLogger().log(Level.INFO, "Skipped no found enchantment group {0}", targets[i]);
            }
    }

    @Override
    public boolean canTarget(List<Material> materials) {
        for (EnchantmentTarget target : targets) {
            if (target == EnchantmentTarget.ALL)
                return true;
            for (Material material : materials)
                if (target.includes(material))
                    return true;
        }
        return false;
    }

    @Override
    public boolean canTarget(Material material) {
        for (EnchantmentTarget target : targets) {
            if (target == EnchantmentTarget.ALL)
                return true;
            if (target.includes(material))
                return true;
        }
        return false;
    }

    @Override
    public boolean canTarget(EnchantmentTarget target) {
        if(target == EnchantmentTarget.ALL)
            return true;
        for(EnchantmentTarget checkingTarget : targets)
            if (checkingTarget == target || checkingTarget == EnchantmentTarget.ALL)
                return true;
        return false;
    }

    @Override
    public boolean canTarget(ITargetWrapper wrapper) {
        for (EnchantmentTarget target : targets) {
            if (target == EnchantmentTarget.ALL)
                return true;
            if (wrapper.canTarget(target))
                return true;
        }
        return false;
    }
}
