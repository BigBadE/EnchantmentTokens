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

package com.bigbade.enchantmenttokens.utils.enchants;

import com.bigbade.enchantmenttokens.api.EnchantmentBase;
import com.bigbade.enchantmenttokens.api.SkriptEnchantments;
import com.bigbade.enchantmenttokens.configuration.ConfigurationManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SkriptEnchantmentHandler implements SkriptEnchantments {
    @Getter
    private final List<EnchantmentBase> skriptEnchantments = new ArrayList<>();

    private final EnchantmentHandler enchantmentHandler;
    private final FileConfiguration configuration;

    @Override
    public void registerSkriptEnchant(EnchantmentBase enchantment) {
        for (Field field : enchantment.getClass().getSuperclass().getDeclaredFields()) {
            ConfigurationManager.loadConfigForField(field, ConfigurationManager.getSectionOrCreate(configuration,
                    enchantment.getKey().getKey()), enchantment);
        }
        enchantment.loadConfig();
        skriptEnchantments.add(enchantment);
        enchantmentHandler.registerEnchant(enchantment);
    }

    @Override
    public void registerEnchantments() {
        CustomEnchantmentHandler.registerEnchantments(skriptEnchantments);
    }
}
