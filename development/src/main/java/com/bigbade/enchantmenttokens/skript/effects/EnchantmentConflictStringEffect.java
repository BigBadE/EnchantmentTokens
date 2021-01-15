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
import com.bigbade.enchantmenttokens.EnchantmentTokens;
import com.bigbade.enchantmenttokens.skript.SkriptEnchantment;
import org.bukkit.event.Event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Level;
import java.util.regex.Pattern;

@Name("EnchantmentTarget")
@Description({"Sets the enchantment target, two conflicting enchantments cannot both be added to the same item",
        "Ex: Infinity and Mending.", "The string should be the key of the enchantment",
        "it should be namespace:enchantname",
        "vanilla enchants have the namespace 'minecraft', Skript enchantments are 'skript'"})
@Examples({"on Skript start:",
        "\u0009set the conflict of Test to skript:myenchant"})
public class EnchantmentConflictStringEffect extends Effect {
    static {
        Skript.registerEffect(EnchantmentConflictEffect.class,
                "set [the] conflict of %customenchant% to %string%");
    }

    private Expression<SkriptEnchantment> enchantment;
    private Expression<String> conflict;

    private static final Pattern splitPattern = Pattern.compile(":");

    @Override
    protected void execute(@Nonnull Event event) {
        String conflictEnchant = conflict.getSingle(event);
        String[] split = splitPattern.split(conflictEnchant);
        SkriptEnchantment skriptEnchantment = enchantment.getSingle(event);
        assert skriptEnchantment != null;
        if (split.length != 2) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Enchantment {0} has incorrect conflict {1}",
                    new Object[]{skriptEnchantment.getEnchantmentName(), conflictEnchant});
        }
        skriptEnchantment.addConflict(split[0], split[1]);
    }

    @Nonnull
    @Override
    public String toString(@Nullable Event event, boolean b) {
        if(event == null) {
            return "Set conflict of enchantment to another enchantment";
        }
        SkriptEnchantment foundEnchantment = enchantment.getSingle(event);
        String conflictString = conflict.getSingle(event);
        if(foundEnchantment == null || conflictString == null) {
            return "Set conflict of enchantment to another enchantment";
        }
        return "Set conflict of " + foundEnchantment.getEnchantmentName()
                + " to " + conflictString;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(@Nonnull Expression<?>[] expressions, int i, @Nonnull Kleenean kleenean,
                        @Nonnull SkriptParser.ParseResult parseResult) {
        enchantment = (Expression<SkriptEnchantment>) expressions[0];
        conflict = (Expression<String>) expressions[1];
        return true;
    }
}
