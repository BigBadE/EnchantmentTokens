package software.bigbade.enchantmenttokens.api.wrappers;

import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentTarget;
import software.bigbade.enchantmenttokens.EnchantmentTokens;

import java.util.List;
import java.util.logging.Level;

public class EnchantmentTargetWrapper implements ITargetWrapper {
    private EnchantmentTarget[] targets;

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
            if(checkingTarget == target)
                return true;
        return false;
    }

    @Override
    public boolean canTarget(ITargetWrapper wrapper) {
        for (EnchantmentTarget target : targets) {
            if(wrapper.canTarget(target))
                return true;
        }
        return false;
    }
}
