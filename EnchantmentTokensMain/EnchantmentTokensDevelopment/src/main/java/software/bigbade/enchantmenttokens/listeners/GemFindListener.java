/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.configuration.ConfigurationType;
import software.bigbade.enchantmenttokens.localization.TranslatedStringMessage;
import software.bigbade.enchantmenttokens.utils.players.PlayerHandler;

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
            event.getPlayer().sendMessage(new TranslatedStringMessage(player.getLanguage(), StringUtils.GEMS_FIND).translate("" + gems));
        }
    }

    private long getRandomGems(EnchantmentPlayer player) {
        return (long) (Math.abs(random.nextGaussian()) * doubler + min) * player.getDoubler();
    }
}
