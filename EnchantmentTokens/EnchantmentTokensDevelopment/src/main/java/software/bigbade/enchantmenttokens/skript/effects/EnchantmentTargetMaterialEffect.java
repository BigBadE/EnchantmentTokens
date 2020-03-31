/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import software.bigbade.enchantmenttokens.api.wrappers.MaterialTargetWrapper;
import software.bigbade.enchantmenttokens.skript.SkriptEnchantment;

@Name("EnchantmentTarget")
@Description("Allows you to set the item target of an enchantment, required to be functional in game.")
@Examples({"on Skript start:",
        "	set target of Test to a shield"})
public class EnchantmentTargetMaterialEffect extends Effect {
    static {
        Skript.registerEffect(EnchantmentTargetMaterialEffect.class, "Set target of %customenchant% to [a] %itemtypes%");
    }

    private Expression<SkriptEnchantment> enchantment;
    private Expression<ItemType> itemType;

    @Override
    protected void execute(Event event) {
        SkriptEnchantment skriptEnchantment = enchantment.getSingle(event);
        for (ItemType type : itemType.getAll(event))
            skriptEnchantment.setTarget(new MaterialTargetWrapper(type.getMaterial()));
    }

    @Override
    public String toString(Event event, boolean b) {
        return "Set material of " + enchantment.getSingle(event).getEnchantmentName() + " to " + itemType.getSingle(event).toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        enchantment = (Expression<SkriptEnchantment>) expressions[0];
        itemType = (Expression<ItemType>) expressions[1];
        return true;
    }
}
