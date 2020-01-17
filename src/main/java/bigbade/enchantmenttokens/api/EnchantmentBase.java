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

    private EnchantmentTarget target;
    private final List<Material> targets = new ArrayList<>();
    private boolean treasure = false;
    private final List<Enchantment> conflicts = new ArrayList<>();
    private final Map<ListenerType, Consumer<Event>> listeners = new HashMap<>();
    private final Material icon;

    @ConfigurationField
    public String name;

    @ConfigurationField
    public int maxLevel = 3;
    @ConfigurationField
    public int minLevel = 1;

    @ConfigurationField
    public ConfigurationSection price;

    public EnchantmentBase(String name, Material icon) {
        super(new NamespacedKey("enchantmenttokens", name.toLowerCase()));
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

    public void loadConfig() {
        String priceIncreaseType = price.getString("type");
        if (priceIncreaseType != null)
            for (PriceIncreaseTypes types : PriceIncreaseTypes.values()) {
                if (priceIncreaseType.toUpperCase().replace(" ", "").equals(types.name())) {
                    types.loadConfig(this);
                    return;
                }
            }
        price.set("type", PriceIncreaseTypes.CUSTOM.name().toLowerCase());
        PriceIncreaseTypes.CUSTOM.loadConfig(this);
    }

    @Override
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
        else if (target != null)
            return target.includes(itemStack.getType());
        else
            return false;
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
