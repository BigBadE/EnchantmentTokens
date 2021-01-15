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
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import com.bigbade.enchantmenttokens.api.wrappers.MaterialTargetWrapper;
import com.bigbade.enchantmenttokens.skript.SkriptEnchantment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

@Name("EnchantmentTarget")
@Description("Allows you to set the item target of an enchantment, required to be functional in game.")
@Examples({"on Skript start:",
        "\u0009set target of Test to a shield"})
public class EnchantmentTargetMaterialEffect extends Effect {
    static {
        Skript.registerEffect(EnchantmentTargetMaterialEffect.class,
                "Set target of %customenchant% to [a] %itemtypes%");
    }

    private Expression<SkriptEnchantment> enchantmentExpression;
    private Expression<ItemType> itemType;

    @Override
    protected void execute(@Nonnull Event event) {
        SkriptEnchantment skriptEnchantment = enchantmentExpression.getSingle(event);
        Objects.requireNonNull(skriptEnchantment);
        for (ItemType type : itemType.getAll(event)) {
            skriptEnchantment.setTarget(new MaterialTargetWrapper(type.getMaterial()));
        }
    }

    @Override
    @Nonnull
    public String toString(@Nullable Event event, boolean b) {
        if(event == null) {
            return "Set material of enchantment to itemtype";
        }
        SkriptEnchantment enchantment = enchantmentExpression.getSingle(event);
        ItemType type = itemType.getSingle(event);
        if(enchantment == null || type == null) {
            return "Set material of enchantment to itemtype";
        }
        return "Set material of " + enchantment.getEnchantmentName() + " to " + type.toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(@Nonnull Expression<?>[] expressions, int i, @Nonnull Kleenean kleenean, @Nonnull SkriptParser.ParseResult parseResult) {
        enchantmentExpression = (Expression<SkriptEnchantment>) expressions[0];
        itemType = (Expression<ItemType>) expressions[1];
        return true;
    }
}
