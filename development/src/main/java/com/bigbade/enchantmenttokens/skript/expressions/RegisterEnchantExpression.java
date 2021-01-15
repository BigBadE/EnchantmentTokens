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

package com.bigbade.enchantmenttokens.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.bigbade.enchantmenttokens.EnchantmentTokens;
import com.bigbade.enchantmenttokens.api.EnchantmentBase;
import com.bigbade.enchantmenttokens.api.SkriptEnchantments;
import com.bigbade.enchantmenttokens.skript.SkriptEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Name("Register enchantment")
@Description("Registers an enchantment with given name.")
@Examples({"on Skript start:",
        "\u0009create a new custom enchant named \"Test\" with an icon of command block "})
public class RegisterEnchantExpression extends SimpleExpression<SkriptEnchantment> {
    static {
        Skript.registerExpression(RegisterEnchantExpression.class, SkriptEnchantment.class, ExpressionType.COMBINED, "[create] [a] [new] custom enchant[ment] named %string% with [an] icon [of] %itemtype%");
    }

    private Expression<String> name;
    private Expression<ItemType> iconExpression;
    private SkriptEnchantments skriptHandler;

    @Override
    @Nonnull
    public String toString(@Nullable Event event, boolean b) {
        if (event == null) {
            return "Registering enchantment";
        }
        return "Registering enchantment " + name.getSingle(event);
    }

    @Override
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public boolean init(@Nonnull Expression<?>[] expressions, int i, @Nonnull Kleenean kleenean,
                        @Nonnull SkriptParser.ParseResult parseResult) {
        name = (Expression<String>) expressions[0];
        iconExpression = (Expression<ItemType>) expressions[1];
        skriptHandler = ((EnchantmentTokens) Bukkit.getPluginManager().getPlugin("EnchantmentTokens"))
                .getEnchantmentHandler().getSkriptEnchantmentHandler().orElseThrow(
                        () -> new IllegalStateException("Tried to parse Skript condition without Skript installed!"));
        return true;
    }

    @Override
    protected SkriptEnchantment[] get(@Nonnull Event event) {
        String nameStr = name.getSingle(event);
        ItemType icon = this.iconExpression.getSingle(event);
        assert icon != null;

        for (EnchantmentBase enchantment : skriptHandler.getSkriptEnchantments()) {
            if (enchantment.getKey().getKey().equals(nameStr)) {
                enchantment.setIcon(icon.getMaterial());
                return new SkriptEnchantment[]{(SkriptEnchantment) enchantment};
            }
        }
        return new SkriptEnchantment[]{null};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    @Nonnull
    public Class<? extends SkriptEnchantment> getReturnType() {
        return SkriptEnchantment.class;
    }
}
