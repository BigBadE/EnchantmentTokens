/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.utils.players;

import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.api.wrappers.EnchantmentChain;
import software.bigbade.enchantmenttokens.currency.CurrencyFactory;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class EnchantmentPlayerHandler implements PlayerHandler {
    private final Map<UUID, EnchantmentPlayer> players = new HashMap<>();
    private final CurrencyFactory currencyFactory;

    private int taskId;

    public EnchantmentPlayerHandler(CurrencyFactory currencyFactory) {
        this.currencyFactory = currencyFactory;
    }

    public EnchantmentPlayer loadPlayer(Player player) {
        EnchantmentPlayer enchantmentPlayer = new CustomEnchantmentPlayer(player);
        enchantmentPlayer.setCurrencyHandler(currencyFactory.newInstance(player));
        players.put(player.getUniqueId(), enchantmentPlayer);
        return enchantmentPlayer;
    }

    public EnchantmentPlayer getPlayer(Player player) {
        EnchantmentPlayer enchantmentPlayer = players.get(player.getUniqueId());
        if (enchantmentPlayer == null) {
            return loadPlayer(player);
        } else {
            return enchantmentPlayer;
        }
    }

    public void removePlayer(EnchantmentPlayer player) {
        if (players.containsKey(player.getPlayer().getUniqueId())) {
            new EnchantmentChain(player.getPlayer().getUniqueId().toString()).async(player::save).execute();
            players.remove(player.getPlayer().getUniqueId());
        }
    }

    public void autosave(SchedulerHandler handler) {
        if (!players.isEmpty()) {
            List<EnchantmentPlayer> entryList = new ArrayList<>(players.values());
            AtomicInteger saving = new AtomicInteger(0);
            taskId = handler.runTaskAsyncRepeating(() -> {
                if (saving.get() < players.size()) {
                    EnchantmentPlayer player = entryList.get(saving.getAndIncrement());
                    player.save();
                    removePlayer(player);
                } else {
                    handler.cancel(taskId);
                }
            }, 0, entryList.size() / 15 * 60 * 20);
        }
    }

    public void shutdown() {
        players.forEach((key, value) -> value.save());
    }
}
