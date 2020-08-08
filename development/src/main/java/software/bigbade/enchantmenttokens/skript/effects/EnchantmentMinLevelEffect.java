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

@Name("EnchantmentMinLevel")
@Description("Allows you to set the default min level of the enchantment")
@Examples({"on Skript start:",
        "\u0009set the min level of Test to 5"})
public class EnchantmentMinLevelEffect extends Effect {
    static {
        Skript.registerEffect(EnchantmentMinLevelEffect.class, "Set [the] [min][minimum] level of %customenchant% to %number%");
    }

    private Expression<SkriptEnchantment> enchantment;
    private Expression<Number> minLevel;

    @Override
    protected void execute(@Nonnull Event event) {
        int newLevel = Objects.requireNonNull(this.minLevel.getSingle(event)).intValue();
        Objects.requireNonNull(enchantment.getSingle(event)).setStartLevel(newLevel);
    }

    @Nonnull
    @Override
    public String toString(Event event, boolean b) {
        return "Set min level of " + Objects.requireNonNull(enchantment.getSingle(event)).getEnchantmentName() + " to " + minLevel.getSingle(event);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(@Nonnull Expression<?>[] expressions, int i, @Nonnull Kleenean kleenean, @Nonnull SkriptParser.ParseResult parseResult) {
        enchantment = (Expression<SkriptEnchantment>) expressions[0];
        minLevel = (Expression<Number>) expressions[1];
        return true;
    }
}
