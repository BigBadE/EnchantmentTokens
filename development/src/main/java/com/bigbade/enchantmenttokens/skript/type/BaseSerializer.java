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

import ch.njol.skript.classes.Serializer;
import ch.njol.yggdrasil.Fields;
import com.bigbade.enchantmenttokens.EnchantmentTokens;
import com.bigbade.enchantmenttokens.api.EnchantmentBase;
import com.bigbade.enchantmenttokens.api.SkriptEnchantments;
import com.bigbade.enchantmenttokens.skript.SkriptEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import javax.annotation.Nonnull;
import java.io.StreamCorruptedException;
import java.util.Objects;

public class BaseSerializer extends Serializer<SkriptEnchantment> {
    @SuppressWarnings("ConstantConditions")
    SkriptEnchantments skriptHandler = ((EnchantmentTokens) Bukkit.getPluginManager().getPlugin("EnchantmentTokens"))
            .getEnchantmentHandler().getSkriptEnchantmentHandler().orElseThrow(
                    () -> new IllegalStateException("Tried to parse Skript condition without Skript installed!"));

    @Override
    @Nonnull
    public Fields serialize(final SkriptEnchantment enchantment) {
        Fields fields = new Fields();
        fields.putObject("key", enchantment.getKey());
        return fields;
    }

    @Override
    public boolean canBeInstantiated() {
        return false;
    }

    @Override
    public void deserialize(@Nonnull final SkriptEnchantment enchantment, @Nonnull final Fields field) {
        throw new UnsupportedOperationException("Enchantments should NOT be saved!");
    }

    @Override
    @Nonnull
    protected SkriptEnchantment deserialize(final Fields fields) throws StreamCorruptedException {
        NamespacedKey key = (NamespacedKey) fields.getObject("key");
        for (EnchantmentBase enchantment : skriptHandler.getSkriptEnchantments()) {
            if (enchantment.getKey().equals(key)) {
                return (SkriptEnchantment) enchantment;
            }
        }
        Objects.requireNonNull(key);
        SkriptEnchantment enchantment = new SkriptEnchantment(key, key.getKey(), Material.BEDROCK);
        skriptHandler.registerSkriptEnchant(enchantment);
        return enchantment;
    }

    @Override
    public boolean mustSyncDeserialization() {
        return false;
    }
}
