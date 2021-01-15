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
import com.bigbade.enchantmenttokens.utils.RegexPatterns;
import org.bukkit.event.Event;
import com.bigbade.enchantmenttokens.EnchantmentTokens;
import com.bigbade.enchantmenttokens.api.wrappers.MaterialTargetWrapper;
import com.bigbade.enchantmenttokens.skript.SkriptEnchantment;
import com.bigbade.enchantmenttokens.utils.MaterialGroupUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.logging.Level;

@Name("EnchantmentTarget")
@Description("Allows you to set the item target of an enchantment, required to be functional in game. " +
        "Types: Tools, Armor, Boots, Leggings, Chestplates, " +
        "Helmets, Weapon, Bow, Fishing rod, Wearable, Trident, Crossbow")
@Examples({"on Skript start:",
        "\u0009set the target of Test to \"Pickaxe\""})
public class EnchantmentTargetEffect extends Effect {
    static {
        Skript.registerEffect(EnchantmentTargetEffect.class, "Set [the] target of %customenchant% to %string%");
    }

    private Expression<SkriptEnchantment> enchantmentExpression;
    private Expression<String> target;

    @Override
    protected void execute(@Nonnull Event event) {
        String type = target.getSingle(event);
        SkriptEnchantment enchantment = enchantmentExpression.getSingle(event);
        Objects.requireNonNull(type);
        Objects.requireNonNull(enchantment);
        try {
            enchantment.setTarget(new MaterialTargetWrapper(MaterialGroupUtils.valueOf(RegexPatterns.SPACE_PATTERN
                    .matcher(type.toUpperCase()).replaceAll("_")).getMaterials()));
        } catch (IllegalArgumentException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Type {0} doesnt exist!", type);
        }
    }

    @Nonnull
    @Override
    public String toString(@Nullable Event event, boolean b) {
        if(event == null) {
            return "set material of enchantment to material";
        }
        String type = target.getSingle(event);
        SkriptEnchantment enchantment = enchantmentExpression.getSingle(event);
        if(enchantment == null) {
            return "Set material of enchantment to " + type;
        }
        return "Set material of " + enchantment.getEnchantmentName() + " to " + type;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(@Nonnull Expression<?>[] expressions, int i, @Nonnull Kleenean kleenean,
                        @Nonnull SkriptParser.ParseResult parseResult) {
        enchantmentExpression = (Expression<SkriptEnchantment>) expressions[0];
        target = (Expression<String>) expressions[1];
        return true;
    }
}
