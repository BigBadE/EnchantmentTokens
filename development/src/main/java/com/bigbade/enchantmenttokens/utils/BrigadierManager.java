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

package com.bigbade.enchantmenttokens.utils;

import com.mojang.brigadier.tree.LiteralCommandNode;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import me.lucko.commodore.file.CommodoreFileFormat;
import org.bukkit.command.Command;
import com.bigbade.enchantmenttokens.EnchantmentTokens;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

public final class BrigadierManager {

    //Private constructor to hide implicit public one.
    private BrigadierManager() {
    }

    public static void register(EnchantmentTokens tokens, Command command, String permission) {
        Commodore commodore = CommodoreProvider.getCommodore(tokens);
        try (InputStream is = BrigadierManager.class.getResourceAsStream("/commodore/" + command.getName() + ".commodore")) {
            if (is == null) {
                return;
            }
            LiteralCommandNode<?> commandNode = CommodoreFileFormat.parse(is);
            commodore.register(command, commandNode, player ->
                    (permission != null && player.hasPermission(permission)) || player.isOp());
        } catch (IOException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Error loading commodore", e);
        }
    }
}
