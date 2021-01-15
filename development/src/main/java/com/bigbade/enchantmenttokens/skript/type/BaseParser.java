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

package com.bigbade.enchantmenttokens.skript.type;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.util.EnchantmentType;
import com.bigbade.enchantmenttokens.EnchantmentTokens;
import com.bigbade.enchantmenttokens.api.EnchantmentBase;
import com.bigbade.enchantmenttokens.api.SkriptEnchantments;
import com.bigbade.enchantmenttokens.skript.SkriptEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class BaseParser extends Parser<SkriptEnchantment> {
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-z0-9/._-]+");

    private final SkriptEnchantments skriptHandler;

    @SuppressWarnings("ConstantConditions")
    public BaseParser() {
         skriptHandler = ((EnchantmentTokens) Bukkit.getPluginManager().getPlugin("EnchantmentTokens"))
                .getEnchantmentHandler().getSkriptEnchantmentHandler().orElseThrow(
                        () -> new IllegalStateException("Tried to parse Skript condition without Skript installed!"));
    }

    @SuppressWarnings("NullableProblems")
    @Override
    @Nullable
    public SkriptEnchantment parse(@Nonnull final String name,
                                   @Nonnull final ParseContext context) {
        for (EnchantmentBase enchantment : skriptHandler.getSkriptEnchantments()) {
            if (enchantment.getKey().getKey().equals(name)) {
                return (SkriptEnchantment) enchantment;
            }
        }

        if (!NAME_PATTERN.matcher(name.toLowerCase()).matches()) {
            EnchantmentTokens.getEnchantLogger().log(
                    Level.SEVERE, "{0} has invalid characters, must have a-z, 0-9, and some symbols(._-]+)", name);
            return null;
        }

        for (EnchantmentBase enchantment : skriptHandler.getSkriptEnchantments()) {
            if (enchantment.getKey().getKey().equalsIgnoreCase(name)) {
                return (SkriptEnchantment) enchantment;
            }
        }
        SkriptEnchantment enchantment = new SkriptEnchantment(
                new NamespacedKey(Skript.getInstance(), name), name, Material.BEDROCK);
        skriptHandler.registerSkriptEnchant(enchantment);
        return enchantment;
    }

    @Override
    @Nonnull
    public String toString(@Nonnull final SkriptEnchantment enchantment,
                           final int flags) {
        return EnchantmentType.toString(enchantment, flags);
    }

    @Override
    @Nonnull
    public String toVariableNameString(final SkriptEnchantment e) {
        return "" + e.getKey().getKey();
    }

    @Override
    @Nonnull
    public String getVariableNamePattern() {
        return "._-]+";
    }

    @Override
    public boolean canParse(@Nonnull ParseContext context) {
        return context != ParseContext.CONFIG;
    }
}
