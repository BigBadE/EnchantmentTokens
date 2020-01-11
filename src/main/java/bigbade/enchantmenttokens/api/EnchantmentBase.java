package bigbade.enchantmenttokens.api;

/*
EnchantmentTokens
Copyright (C) 2019-2020 Big_Bad_E
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

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

    @ConfigurationField
    public ConfigurationSection price;

    public EnchantmentBase(EnchantmentTokens main, String name, ConfigurationSection config, Material icon) {
        super(new NamespacedKey(main, name.toLowerCase()));
        this.config = config;
        this.main = main;
        this.name = name;
        this.icon = icon;
    }

    public long getDefaultPrice(int level) {
        String priceIncreaseType = price.getString("type");
        if (priceIncreaseType != null)
            for (PriceIncreaseTypes types : PriceIncreaseTypes.values()) {
                if (priceIncreaseType.toUpperCase().replace(" ", "").equals(types.name())) {
                    return types.getPrice(level, price);
                }
            }
        return PriceIncreaseTypes.CUSTOM.getPrice(level, price);
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
