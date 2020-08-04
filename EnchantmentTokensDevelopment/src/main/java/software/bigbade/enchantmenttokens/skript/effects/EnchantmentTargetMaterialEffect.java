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

import javax.annotation.Nonnull;
import java.util.Objects;

@Name("EnchantmentTarget")
@Description("Allows you to set the item target of an enchantment, required to be functional in game.")
@Examples({"on Skript start:",
        "	set target of Test to a shield"})
public class EnchantmentTargetMaterialEffect extends Effect {
    static {
        Skript.registerEffect(EnchantmentTargetMaterialEffect.class, "Set target of %customenchant% to [a] %itemtypes%");
    }

    private Expression<SkriptEnchantment> enchantmentExpression;
    private Expression<ItemType> itemType;

    @Override
    protected void execute(@Nonnull Event event) {
        SkriptEnchantment skriptEnchantment = enchantmentExpression.getSingle(event);
        Objects.requireNonNull(skriptEnchantment);
        for (ItemType type : itemType.getAll(event))
            skriptEnchantment.setTarget(new MaterialTargetWrapper(type.getMaterial()));
    }

    @Override
    @Nonnull
    public String toString(Event event, boolean b) {
        SkriptEnchantment enchantment = enchantmentExpression.getSingle(event);
        ItemType type = itemType.getSingle(event);
        Objects.requireNonNull(enchantment);
        Objects.requireNonNull(type);
        return "Set material of " + enchantment.getEnchantmentName() + " to " + type.toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int i, @Nonnull Kleenean kleenean, @Nonnull SkriptParser.ParseResult parseResult) {
        enchantmentExpression = (Expression<SkriptEnchantment>) expressions[0];
        itemType = (Expression<ItemType>) expressions[1];
        return true;
    }
}
