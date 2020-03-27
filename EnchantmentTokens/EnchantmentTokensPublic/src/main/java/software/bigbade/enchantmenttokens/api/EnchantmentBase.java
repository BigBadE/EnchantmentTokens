package software.bigbade.enchantmenttokens.api;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import software.bigbade.enchantmenttokens.api.wrappers.ITargetWrapper;

import javax.annotation.Nonnull;

public abstract class EnchantmentBase extends Enchantment {
    @ConfigurationField
    private String name;
    @ConfigurationField
    private Material icon;

    @SuppressWarnings("ConstantConditions")
    public EnchantmentBase(@Nonnull NamespacedKey key, @Nonnull Material icon, String defaultName) {
        super(key);
        if(name == null)
            setName(defaultName);
        if(getIcon() == null)
            setIcon(icon);
    }

    private void setName(String name) {
        this.name = name;
    }

    public Material getIcon() {
        return icon;
    }

    public void setIcon(Material icon) {
        this.icon = icon;
    }

    public abstract void onDisable();

    public abstract long getDefaultPrice(int level);

    public abstract void loadConfig();

    public abstract Enchantment getEnchantment();

    public abstract ConfigurationSection getPriceSection();

    public abstract ITargetWrapper getTargets();

    @Nonnull
    public String getEnchantName() {
        return name;
    }

    @Override
    @Nonnull
    @Deprecated
    public String getName() {
        return getEnchantName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CustomEnchantment)
            return hashCode() == obj.hashCode();
        return false;
    }

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }
}
