package software.bigbade.enchantmenttokens.listeners;

import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {
    private EnchantmentPlayerHandler playerHandler;

    public PlayerLeaveListener(EnchantmentPlayerHandler handler) {
        this.playerHandler = handler;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        savePlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        savePlayer(event.getPlayer());
    }

    private void savePlayer(Player player) {
        EnchantmentPlayer enchantmentPlayer = playerHandler.getPlayer(player);
        enchantmentPlayer.save(false);
        playerHandler.removePlayer(enchantmentPlayer);
    }
}
