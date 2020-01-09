package bigbade.enchantmenttokens.api;

import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentTarget;

import java.util.Arrays;
import java.util.List;

public enum ListenerType {
    //On block starting hit
    BLOCKDAMAGED(EnchantmentTarget.TOOL),
    //On block destroyed
    BLOCKBREAK(EnchantmentTarget.TOOL),
    //Item swapped to
    EQUIP(EnchantmentTarget.ALL),
    //Item swapped off
    UNEQUIP(EnchantmentTarget.ALL),
    //On current enchantment applied to an item
    ENCHANT(EnchantmentTarget.ALL);
    //TODO

    private List<EnchantmentTarget> targets;

    ListenerType(EnchantmentTarget... targets) {
        this.targets = Arrays.asList(targets);
    }

    public boolean canTarget(EnchantmentTarget target) {
        return targets.contains(target);
    }

    public boolean canTarget(Material material) {
        return targets.stream().anyMatch((type) -> type.includes(material));
    }
}
