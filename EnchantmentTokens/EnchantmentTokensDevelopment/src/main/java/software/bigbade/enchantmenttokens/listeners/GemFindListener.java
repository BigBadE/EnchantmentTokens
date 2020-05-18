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
    private final ConfigurationSection section;
    private final PlayerHandler playerHandler;
    private final Random random = new Random();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (section != null && random.nextDouble() <= new ConfigurationType<>(0.05).getValue("chance", section)) {
            assert playerHandler != null;
            EnchantmentPlayer player = playerHandler.getPlayer(event.getPlayer());
            long gems = getRandomGems(player);
            player.addGems(gems);
            event.getPlayer().sendMessage(new TranslatedStringMessage(player.getLanguage(), StringUtils.GEMS_FIND).translate("" + gems));
        }
    }

    private long getRandomGems(EnchantmentPlayer player) {
        return (long) (Math.abs(random.nextGaussian()) * new ConfigurationType<>(1).getValue("doubler", section) + new ConfigurationType<>(1).getValue("minimum", section)) * player.getDoubler();
    }
}
