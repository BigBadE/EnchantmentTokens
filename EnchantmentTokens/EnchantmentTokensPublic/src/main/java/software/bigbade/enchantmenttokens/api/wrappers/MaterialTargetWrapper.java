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

import java.util.List;

public class MaterialTargetWrapper implements ITargetWrapper {
    private Material[] materials;

    public MaterialTargetWrapper(String... materials) {
        this.materials = new Material[materials.length];
        for(int i = 0; i < materials.length; i++)
            this.materials[i] = Material.getMaterial(materials[i]);
    }

    public MaterialTargetWrapper(Material... materials) {
        this.materials = materials;
    }

    @Override
    public boolean canTarget(List<Material> checking) {
        for(Material material : materials)
            if(checking.contains(material))
                return true;
        return false;
    }

    @Override
    public boolean canTarget(Material material) {
        for(Material targetMaterial : materials)
            if(targetMaterial == material)
                return true;
        return false;
    }

    @Override
    public boolean canTarget(EnchantmentTarget target) {
        for(Material material : materials)
            if(target.includes(material))
                return true;
        return false;
    }

    @Override
    public boolean canTarget(ITargetWrapper wrapper) {
        for(Material material : materials)
            if(wrapper.canTarget(material))
                return true;
        return false;
    }
}
