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
import java.util.Objects;

@Name("EnchantmentMaxLevel")
@Description("Allows you to set the default max level of the enchantment")
@Examples({"on Skript start:",
        "\u0009set the max level of Test to 5"})
public class EnchantmentMaxLevelEffect extends Effect {
    static {
        Skript.registerEffect(EnchantmentMaxLevelEffect.class, "Set [the] [max][maximum] level of %customenchant% to %number%");
    }

    private Expression<SkriptEnchantment> enchantment;
    private Expression<Number> maxLevel;

    @Override
    protected void execute(@Nonnull Event event) {
        int minLevel = Objects.requireNonNull(this.maxLevel.getSingle(event)).intValue();
        Objects.requireNonNull(enchantment.getSingle(event)).setMaxLevel(minLevel);
    }

    @Nonnull
    @Override
    public String toString(Event event, boolean b) {
        return "Set max level of " + Objects.requireNonNull(enchantment.getSingle(event)).getEnchantmentName() + " to " + maxLevel.getSingle(event);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(@Nonnull Expression<?>[] expressions, int i, @Nonnull Kleenean kleenean, @Nonnull SkriptParser.ParseResult parseResult) {
        enchantment = (Expression<SkriptEnchantment>) expressions[0];
        maxLevel = (Expression<Number>) expressions[1];
        return true;
    }
}
