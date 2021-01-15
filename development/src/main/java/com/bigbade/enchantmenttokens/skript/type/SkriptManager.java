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

import ch.njol.skript.SkriptAddon;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.registrations.Classes;
import com.bigbade.enchantmenttokens.EnchantmentTokens;
import com.bigbade.enchantmenttokens.skript.SkriptEnchantment;

import java.io.IOException;
import java.util.logging.Level;

public class SkriptManager {
    public SkriptManager(SkriptAddon addon) {
        try {
            Classes.registerClass(new ClassInfo<>(SkriptEnchantment.class, "customenchant").user("CustomEnchantments")
                    .name("CustomEnchantment")
                    .description("A custom enchantment.")
                    .parser(new BaseParser())
                    .serializer(new BaseSerializer()));
            addon.loadClasses("com.bigbade.enchantmenttokens", "skript");
        } catch (IOException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not load Skript compatibility", e);
        }
    }
}
