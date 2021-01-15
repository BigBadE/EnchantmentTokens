/*
 * Custom enchantments for Minecraft
 * Copyright (C) 2021 Big_Bad_E
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bigbade.enchantmenttokens.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.bigbade.enchantmenttokens.skript.SkriptEnchantment;
import org.bukkit.event.Event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Name("EnchantmentMinLevel")
@Description("Allows you to set the default min level of the enchantment")
@Examples({"on Skript start:",
        "\u0009set the min level of Test to 5"})
public class EnchantmentMinLevelEffect extends Effect {
    static {
        Skript.registerEffect(EnchantmentMinLevelEffect.class,
                "Set [the] [min][minimum] level of %customenchant% to %number%");
    }

    private Expression<SkriptEnchantment> enchantment;
    private Expression<Number> minLevel;

    @Override
    protected void execute(@Nonnull Event event) {
        Number newLevel = minLevel.getSingle(event);
        SkriptEnchantment foundEnchantment = enchantment.getSingle(event);
        if(newLevel == null || foundEnchantment == null) {
            Skript.error("Unassigned variable passed to effect!");
            return;
        }
        foundEnchantment.setStartLevel(newLevel.intValue());
    }

    @Nonnull
    @Override
    public String toString(@Nullable Event event, boolean b) {
        if(event == null) {
            return "Set min level of enchantment to number";
        }
        Number newLevel = minLevel.getSingle(event);
        SkriptEnchantment foundEnchantment = enchantment.getSingle(event);
        if(foundEnchantment == null) {
            return "Set min level of enchant to " + newLevel;
        }
        return "Set min level of " + foundEnchantment.getEnchantmentName()
                + " to " + newLevel;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(@Nonnull Expression<?>[] expressions, int i, @Nonnull Kleenean kleenean,
                        @Nonnull SkriptParser.ParseResult parseResult) {
        enchantment = (Expression<SkriptEnchantment>) expressions[0];
        minLevel = (Expression<Number>) expressions[1];
        return true;
    }
}
