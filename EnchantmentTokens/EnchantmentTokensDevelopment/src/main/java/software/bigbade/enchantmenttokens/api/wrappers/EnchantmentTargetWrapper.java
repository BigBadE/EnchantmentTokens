package software.bigbade.enchantmenttokens.api.wrappers;

import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentTarget;
import software.bigbade.enchantmenttokens.utils.EnchantLogger;

import java.util.List;
import java.util.logging.Level;

public class EnchantmentTargetWrapper implements ITargetWrapper {
    private EnchantmentTarget[] targets;

    public EnchantmentTargetWrapper(EnchantmentTarget... targets) {
        this.targets = targets;
    }

    public EnchantmentTargetWrapper(String... targets) {
        this.targets = new EnchantmentTarget[targets.length];
        for(int i = 0; i < targets.length; i++)
            try {
                this.targets[i] = EnchantmentTarget.valueOf(targets[i]);
            } catch (IllegalArgumentException e) {
                EnchantLogger.log(Level.INFO, "Skipped no found enchantment group {0}", targets[i]);
            }
    }

    @Override
    public boolean canTarget(List<Material> materials) {
        for (Material material : materials)
            for (EnchantmentTarget target : targets)
                if (target.includes(material))
                    return true;
        return false;
    }
}