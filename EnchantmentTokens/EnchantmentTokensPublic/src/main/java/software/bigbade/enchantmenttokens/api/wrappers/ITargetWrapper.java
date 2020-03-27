package software.bigbade.enchantmenttokens.api.wrappers;

import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentTarget;

import java.util.List;

public interface ITargetWrapper {
    boolean canTarget(List<Material> materials);

    boolean canTarget(Material material);

    boolean canTarget(EnchantmentTarget target);

    boolean canTarget(ITargetWrapper wrapper);
}
