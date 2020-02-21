package software.bigbade.enchantmenttokens.listeners;

import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private EnchantmentPlayerHandler playerHandler;

    public PlayerJoinListener(EnchantmentPlayerHandler handler) {
        playerHandler = handler;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        playerHandler.getPlayer(event.getPlayer());
    }
}
