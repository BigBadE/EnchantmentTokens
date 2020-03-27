package software.bigbade.enchantmenttokens.api;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.api.wrappers.EnchantmentConflictWrapper;
import software.bigbade.enchantmenttokens.api.wrappers.IConflictWrapper;
import software.bigbade.enchantmenttokens.api.wrappers.ITargetWrapper;

import javax.annotation.Nonnull;

public class CustomEnchantment extends EnchantmentBase {
    private ITargetWrapper targets;
    private IConflictWrapper conflicts = new EnchantmentConflictWrapper();
    private boolean treasure = false;
    private boolean cursed;

    @ConfigurationField
    private int maxLevel = 3;
    @ConfigurationField
    private int minLevel = 1;

    @ConfigurationField
    private ConfigurationSection price = null;

    @ConfigurationField("price")
    private String type = "custom";

    public CustomEnchantment(NamespacedKey key, Material icon, String defaultName) {
        super(key, icon, defaultName);
    }

    @Override
    public Enchantment getEnchantment() {
        return this;
    }

    public void onDisable() {
        //Overridden by subclasses
    }

    public long getDefaultPrice(int level) {
        for (PriceIncreaseTypes types : PriceIncreaseTypes.values()) {
            if (type.toUpperCase().replace(" ", "").equals(types.name())) {
                return types.getPrice(level, price);
            }
        }
        return PriceIncreaseTypes.CUSTOM.getPrice(level, price);
    }

    public void loadConfig() {
        for (PriceIncreaseTypes types : PriceIncreaseTypes.values()) {
            if (type.toUpperCase().replace(" ", "").equals(types.name())) {
                types.loadConfig(this);
                return;
            }
        }
        price.set("type", PriceIncreaseTypes.CUSTOM.name().toLowerCase());
        PriceIncreaseTypes.CUSTOM.loadConfig(this);
    }

    public ConfigurationSection getPriceSection() {
        return price;
    }

    @Nonnull
    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ALL;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public int getStartLevel() {
        return minLevel;
    }

    @Override
    public boolean isTreasure() {
        return treasure;
    }

    @Override
    public boolean conflictsWith(@Nonnull Enchantment enchantment) {
        return conflicts.conflicts(enchantment);
    }

    @Override
    public boolean isCursed() {
        return cursed;
    }

    @Override
    public boolean canEnchantItem(@Nonnull ItemStack itemStack) {
        if (itemStack.getItemMeta() == null || !itemStack.getItemMeta().hasEnchants())
            return targets.canTarget(itemStack.getType());
        for (Enchantment enchantment : itemStack.getEnchantments().keySet())
            if (conflicts.conflicts(enchantment))
                return false;
        return targets.canTarget(itemStack.getType());
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public void setTreasure(boolean treasure) {
        this.treasure = treasure;
    }

    public void addConflict(Enchantment conflict) {
        conflicts.addTarget(conflict);
    }

    public void addConflict(String namespace, String name) {
        conflicts.addTarget(namespace, name);
    }

    public void setTarget(ITargetWrapper target) {
        this.targets = target;
    }

    public ITargetWrapper getTargets() {
        return targets;
    }

    public void setCursed(boolean cursed) {
        this.cursed = cursed;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public void setStartLevel(int minLevel) {
        this.minLevel = minLevel;
    }
}
