package software.bigbade.enchantmenttokens.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.skript.SkriptEnchantment;
import software.bigbade.enchantmenttokens.utils.MaterialGroupUtils;

import java.util.logging.Level;

@Name("EnchantmentTarget")
@Description("Allows you to set the item target of an enchantment, required to be functional in game. " +
        "Types: Tools, Armor, Boots, Leggings, Chestplates, Helmets, Weapon, Bow, Fishing rod, Wearable, Trident, Crossbow")
@Examples({"on Skript start:",
        "	set the target of Test to \"Pickaxe\""})
public class EnchantmentTargetEffect extends Effect {
    static {
        Skript.registerEffect(EnchantmentTargetEffect.class, "Set [the] target of %customenchant% to %string%");
    }

    private Expression<SkriptEnchantment> enchantment;
    private Expression<String> target;

    @Override
    protected void execute(Event event) {
        String type = target.getSingle(event);
        try {
            enchantment.getSingle(event).addTargets(MaterialGroupUtils.valueOf(type.toUpperCase().replace(" ", "_")).getMaterials());
        } catch (IllegalArgumentException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Type {0} doesnt exist!", type);
        }
    }

    @Override
    public String toString(Event event, boolean b) {
        return "Set material of " + enchantment.getSingle(event).getName() + " to " + target.getSingle(event);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        enchantment = (Expression<SkriptEnchantment>) expressions[0];
        target = (Expression<String>) expressions[1];
        return true;
    }
}
