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

@Name("EnchantmentMaxLevel")
@Description("Allows you to set the default max level of the enchantment")
@Examples({"on Skript start:",
        "	set the max level of Test to 5"})
public class EnchantmentMaxLevelEffect extends Effect {
    static {
        Skript.registerEffect(EnchantmentMaxLevelEffect.class, "Set [the] [max][maximum] level of %customenchant% to %number%");
    }

    private Expression<SkriptEnchantment> enchantment;
    private Expression<Number> maxLevel;

    @Override
    protected void execute(@Nonnull Event event) {
        int minLevel = this.maxLevel.getSingle(event).intValue();
        enchantment.getSingle(event).setMaxLevel(minLevel);
    }

    @Nonnull
    @Override
    public String toString(Event event, boolean b) {
        return "Set max level of " + enchantment.getSingle(event).getEnchantmentName() + " to " + maxLevel.getSingle(event);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        enchantment = (Expression<SkriptEnchantment>) expressions[0];
        maxLevel = (Expression<Number>) expressions[1];
        return true;
    }
}
