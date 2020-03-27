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
