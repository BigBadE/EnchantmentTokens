/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

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
import software.bigbade.enchantmenttokens.skript.SkriptEnchantment;

import javax.annotation.Nonnull;

@Name("EnchantmentConflict")
@Description("Sets the enchantment conflict, two conflicting enchantments cannot both be added to the same item. Ex: Infinity and Mending")
@Examples({"on Skript start:",
        "	set the conflict of Test to MyEnchant"})
public class EnchantmentConflictEffect extends Effect {
    static {
        Skript.registerEffect(EnchantmentConflictEffect.class, "Set [the] conflict of %customenchant% to %customenchant%");
    }

    private Expression<SkriptEnchantment> enchantment;
    private Expression<SkriptEnchantment> conflict;

    @Override
    protected void execute(@Nonnull Event event) {
        SkriptEnchantment conflictEnchant = conflict.getSingle(event);
        enchantment.getSingle(event).addConflict(conflictEnchant);
    }

    @Nonnull
    @Override
    public String toString(Event event, boolean b) {
        return "Set conflict of " + enchantment.getSingle(event).getEnchantmentName() + " to " + conflict.getSingle(event).getEnchantmentName();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        enchantment = (Expression<SkriptEnchantment>) expressions[0];
        conflict = (Expression<SkriptEnchantment>) expressions[1];
        return true;
    }
}
