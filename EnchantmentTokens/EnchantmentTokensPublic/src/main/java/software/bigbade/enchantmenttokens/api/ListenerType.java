package software.bigbade.enchantmenttokens.api;

import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentTarget;
import software.bigbade.enchantmenttokens.api.wrappers.EnchantmentTargetWrapper;
import software.bigbade.enchantmenttokens.api.wrappers.ITargetWrapper;
import software.bigbade.enchantmenttokens.api.wrappers.MaterialTargetWrapper;

import java.util.List;

public enum ListenerType {
    //On block starting hit
    BLOCK_DAMAGED(new EnchantmentTargetWrapper(EnchantmentTarget.TOOL)),
    //On block destroyed
    BLOCK_BREAK(new EnchantmentTargetWrapper(EnchantmentTarget.TOOL)),
    //Armor equipped
    EQUIP(new EnchantmentTargetWrapper(EnchantmentTarget.ARMOR)),
    //Armor unequipped
    UNEQUIP(new EnchantmentTargetWrapper(EnchantmentTarget.ARMOR)),
    //Item swapped to
    HELD(new EnchantmentTargetWrapper(EnchantmentTarget.ALL)),
    //Item swapped off
    SWAPPED(new EnchantmentTargetWrapper(EnchantmentTarget.ALL)),
    //On current enchantment applied to an item
    ENCHANT(new EnchantmentTargetWrapper(EnchantmentTarget.ALL)),
    //On user death
    DEATH(new EnchantmentTargetWrapper(EnchantmentTarget.ALL)),
    //Potion apply
    POTION_APPLY(new EnchantmentTargetWrapper(EnchantmentTarget.ALL)),
    //Potion remove
    POTION_REMOVE(new EnchantmentTargetWrapper(EnchantmentTarget.ALL)),
    //Shield block
    SHIELD_BLOCK(new MaterialTargetWrapper("SHIELD")),
    //Trident throw
    TRIDENT_THROW(new EnchantmentTargetWrapper("TRIDENT")),
    //Bow shoot
    SHOOT(new EnchantmentTargetWrapper(EnchantmentTarget.BOW)),
    //Crossbow shoot
    CROSSBOW_SHOOT(new EnchantmentTargetWrapper("CROSSBOW")),
    //Entity damage
    DAMAGE(new EnchantmentTargetWrapper(EnchantmentTarget.ALL)),
    //Player riptide with trident
    RIPTIDE(new EnchantmentTargetWrapper("TRIDENT"));

    private ITargetWrapper target;

    ListenerType(ITargetWrapper target) {
        this.target = target;
    }

    public boolean canTarget(List<Material> materials) {
        return target.canTarget(materials);
    }

    public boolean canTarget(ITargetWrapper wrapper) {
        return target.canTarget(wrapper);
    }
}
