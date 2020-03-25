package software.bigbade.enchantmenttokens.utils.players;

import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.currency.CurrencyFactory;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EnchantmentPlayerHandler implements PlayerHandler {
    private List<EnchantmentPlayer> players = new ArrayList<>();
    private CurrencyFactory currencyFactory;

    public EnchantmentPlayerHandler(CurrencyFactory currencyFactory) {
        this.currencyFactory = currencyFactory;
    }

    public EnchantmentPlayer loadPlayer(Player player) {
        EnchantmentPlayer enchantmentPlayer = new CustomEnchantmentPlayer(player);
        enchantmentPlayer.setHandler(currencyFactory.newInstance(player));
        players.add(enchantmentPlayer);
        return enchantmentPlayer;
    }

    public EnchantmentPlayer getPlayer(Player player) {
        for (EnchantmentPlayer enchantmentPlayer : players)
            if (enchantmentPlayer.getPlayer().getUniqueId().equals(player.getUniqueId()))
                return enchantmentPlayer;
        return loadPlayer(player);
    }

    public void removePlayer(EnchantmentPlayer player) {
        players.remove(player);
    }

    public void autosave(SchedulerHandler handler) {
        if (!players.isEmpty()) {
            AtomicInteger saving = new AtomicInteger(0);
            handler.runTaskRepeating(() -> {
                if (saving.get() > players.size()) players.get(saving.getAndIncrement()).save(false);
            }, 0, 5);
        }
    }

    public void shutdown() {
        players.forEach(player -> player.save(true));
    }
}
