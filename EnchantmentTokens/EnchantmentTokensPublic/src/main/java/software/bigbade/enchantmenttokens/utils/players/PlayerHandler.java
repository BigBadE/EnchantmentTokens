package software.bigbade.enchantmenttokens.utils.players;

import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;

public interface PlayerHandler {
    EnchantmentPlayer loadPlayer(Player player);

    EnchantmentPlayer getPlayer(Player player);

    void removePlayer(EnchantmentPlayer player);

    void autosave(SchedulerHandler handler);

    void shutdown();
}
