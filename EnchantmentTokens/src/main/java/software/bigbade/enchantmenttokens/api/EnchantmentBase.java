package software.bigbade.enchantmenttokens.api;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import software.bigbade.enchantmenttokens.utils.enchants.FakePlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class EnchantmentBase extends Enchantment {

    private final List<Material> targets = new ArrayList<>();
    private boolean treasure = false;
    private final List<Enchantment> conflicts = new ArrayList<>();
    private Material icon;
    private boolean cursed;

    @ConfigurationField
    public String name;

    @ConfigurationField
    public int maxLevel = 3;
    @ConfigurationField
    public int minLevel = 1;

    @ConfigurationField
    public ConfigurationSection price;

    @ConfigurationField("price")
    public String type = "custom";

    public EnchantmentBase(String name, Material icon) {
        super(new NamespacedKey(FakePlugin.ENCHANTMENTPLUGIN, name.toLowerCase()));
        this.name = name;
        this.icon = icon;
    }

    public EnchantmentBase(String name, Material icon, String namespace) {
        super(new NamespacedKey(new FakePlugin(namespace), name.toLowerCase()));
        this.name = name;
        this.icon = icon;
    }

    public void onDisable() { }

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

    @NotNull
    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ALL;
    }

    @Override
    @NotNull
    public String getName() {
        return name;
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
    public boolean conflictsWith(@NotNull Enchantment enchantment) {
        return conflicts.contains(enchantment);
    }

    @Override
    public boolean isCursed() {
        return cursed;
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack itemStack) {
        if(!itemStack.hasItemMeta() || !itemStack.getItemMeta().hasEnchants())
            return targets.contains(itemStack.getType());
        for(Enchantment enchantment : itemStack.getEnchantments().keySet())
            if(conflicts.contains(enchantment))
                return false;
        return targets.contains(itemStack.getType());
    }

    public void setTreasure(boolean treasure) {
        this.treasure = treasure;
    }

    public void addConflict(Enchantment conflict) {
        conflicts.add(conflict);
    }

    public void addTargets(Material... targets) {
        this.targets.addAll(Arrays.asList(targets));
    }

    public List<Material> getTargets() { return targets; }

    public Material getIcon() {
        return icon;
    }

    public void setCursed(boolean cursed) {
        this.cursed = cursed;
    }

    public void setIcon(Material icon) {
        this.icon = icon;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EnchantmentBase)
            return hashCode() == obj.hashCode();
        return false;
    }

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }
}
