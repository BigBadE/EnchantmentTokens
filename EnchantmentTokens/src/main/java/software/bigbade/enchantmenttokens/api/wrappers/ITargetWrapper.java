package software.bigbade.enchantmenttokens.api.wrappers;

import org.bukkit.Material;

import java.util.List;

public interface ITargetWrapper {
    boolean canTarget(List<Material> materials);
}
