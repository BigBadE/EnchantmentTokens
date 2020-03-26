package software.bigbade.enchantmenttokens.api;

import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentTarget;

import java.util.Arrays;
import java.util.List;

public enum ListenerType {
    //On block starting hit
    BLOCK_DAMAGED(EnchantmentTarget.TOOL),
    //On block destroyed
    BLOCK_BREAK(EnchantmentTarget.TOOL),
    //Armor equipped
    EQUIP(EnchantmentTarget.ARMOR),
    //Armor unequipped
    UNEQUIP(EnchantmentTarget.ARMOR),
    //Item swapped to
    HELD(EnchantmentTarget.ALL),
    //Item swapped off
    SWAPPED(EnchantmentTarget.ALL),
    //On current enchantment applied to an item
    ENCHANT(EnchantmentTarget.ALL),
    //On user death
    DEATH(EnchantmentTarget.ALL),
    //Potion apply
    POTION_APPLY(EnchantmentTarget.ALL),
    //Potion remove
    POTION_REMOVE(EnchantmentTarget.ALL),
    //Shield block
    SHIELD_BLOCK(EnchantmentTarget.ALL),
    //Trident throw
    TRIDENT_THROW(EnchantmentTarget.TRIDENT),
    //Bow/crossbow shoot
    SHOOT(EnchantmentTarget.BOW, EnchantmentTarget.CROSSBOW),
    //Entity damage
    DAMAGE(EnchantmentTarget.ALL),
    //Player riptide with trident
    RIPTIDE(EnchantmentTarget.TRIDENT);

    private List<EnchantmentTarget> targets;

    ListenerType(EnchantmentTarget... targets) {
        this.targets = Arrays.asList(targets);
    }

    public boolean canTarget(EnchantmentTarget target) {
        if(target == null) return false;
        if (target == EnchantmentTarget.ALL) return true;
        for (EnchantmentTarget found : targets)
            if (found == target)
                return true;
        return false;
    }

    public boolean canTarget(List<Material> materials) {
        return targets.stream().anyMatch(type -> {
            for (Material material : materials) if (type.includes(material)) return true;
            return false;
        });
    }
}
