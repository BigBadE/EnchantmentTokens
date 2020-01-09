package bigbade.enchantmenttokens.api;

import bigbade.enchantmenttokens.EnchantmentTokens;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Consumer;

public abstract class EnchantmentBase extends Enchantment {
    private final String name;
    private EnchantmentTarget target;
    private final List<Material> targets = new ArrayList<>();
    private boolean treasure = false;
    private final List<Enchantment> conflicts = new ArrayList<>();
    private final Map<ListenerType, Consumer<Event>> listeners = new HashMap<>();
    private final Material icon;
    public EnchantmentTokens main;

    public ConfigurationSection config;

    public EnchantmentBase(EnchantmentTokens main, String name, ConfigurationSection config, Material icon) {
        super(new NamespacedKey(main, name.toLowerCase()));
        this.config = config;
        this.main = main;
        this.name = name;
        this.icon = icon;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMaxLevel() {
        return config.getInt("maxLevel");
    }

    @Override
    public int getStartLevel() {
        return config.getInt("minLevel");
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return target;
    }

    public List<Material> getTargets() {
        return targets;
    }

    @Override
    public boolean isTreasure() {
        return treasure;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return conflicts.contains(enchantment);
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        if (targets.size() > 0)
            return targets.contains(itemStack.getType());
        else
            return target.includes(itemStack.getType());
    }

    public void generateDefaultConfig(ConfigurationSection section) {
        section.set("maxLevel", 5);
        section.set("minLevel", 1);
        section.set("type", "custom");
        generateCustomConfig(section);
    }

    public void generateCustomConfig(ConfigurationSection section) {
        ConfigurationSection levels = section.createSection("customLevels");
        for(int i = getStartLevel(); i <= getMaxLevel(); i++) {
            levels.set("" + (i), (i)*10);
        }
    }

    public long getDefaultPrice(int level) {
        String type = config.getString("type");
        if(type == null) {
            config.set("type", "custom");
            type = "custom";
            generateCustomConfig(config);
        }
        switch(type) {
            case "custom":
                return loadCustomConfig(level+1);
            default:
                config.set("type", "custom");
                generateCustomConfig(config);
                return loadCustomConfig(level+1);
        }
    }

    public long loadCustomConfig(int level) {
        ConfigurationSection section = config.getConfigurationSection("customLevels");
        if(section == null) {
            generateCustomConfig(config);
            section = config.getConfigurationSection("customLevels");
        }
        return section.getInt((level-1) + "");
    }

    public Map<ListenerType, Consumer<Event>> getListeners() {
        return listeners;
    }

    protected void registerListener(ListenerType type, Consumer<Event> consumer) {
        listeners.put(type, consumer);
    }

    protected void setTarget(EnchantmentTarget target) {
        this.target = target;
    }

    protected void setTreasure(boolean treasure) {
        this.treasure = treasure;
    }

    protected void addConflict(Enchantment conflict) {
        conflicts.add(conflict);
    }

    protected void addTargets(Material... targets) {
        this.targets.addAll(Arrays.asList(targets));
    }

    public Material getIcon() {
        return icon;
    }
}
