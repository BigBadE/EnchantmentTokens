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

import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import com.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import com.bigbade.enchantmenttokens.configuration.ConfigurationType;
import com.bigbade.enchantmenttokens.localization.LocaleMessages;
import com.bigbade.enchantmenttokens.utils.players.PlayerHandler;

import javax.annotation.Nonnull;
import java.util.Random;

@RequiredArgsConstructor
@Nonnull
public class GemFindListener implements Listener {
    private final PlayerHandler playerHandler;
    private final Random random = new Random();
    private final double chance;
    private final double doubler;
    private final double min;

    public GemFindListener(PlayerHandler playerHandler, double chance, ConfigurationSection section) {
        this.playerHandler = playerHandler;
        this.chance = chance;
        doubler = new ConfigurationType<>(1D).getValue("doubler", section);
        min = new ConfigurationType<>(1).getValue("minimum", section);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (random.nextDouble() <= chance) {
            assert playerHandler != null;
            EnchantmentPlayer player = playerHandler.getPlayer(event.getPlayer());
            long gems = getRandomGems(player);
            player.addGems(gems);
            event.getPlayer().sendMessage(LocaleMessages.GEMS_FIND.translate(player.getLanguage(), gems));
        }
    }

    private long getRandomGems(EnchantmentPlayer player) {
        return (long) (Math.abs(random.nextGaussian()) * doubler + min) * player.getDoubler();
    }
}
