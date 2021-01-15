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

package com.bigbade.enchantmenttokens.listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.Set;

public class ChunkUnloadListener implements Listener {
    private final Set<Location> signs;

    public ChunkUnloadListener(Set<Location> signs) {
        this.signs = signs;
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        signs.removeIf(location -> ((int) location.getX()) << 4 == event.getChunk().getX()
                && ((int) location.getZ()) << 4 == event.getChunk().getZ());
    }
}
