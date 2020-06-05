/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.loader.FileLoader;
import software.bigbade.enchantmenttokens.loader.Pair;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class GemCurrencyHandler extends EnchantCurrencyHandler {
    private final FileLoader fileLoader;
    public GemCurrencyHandler(Player player, FileLoader fileLoader, SchedulerHandler scheduler) {
        super(player, "gemsOld");
        this.fileLoader = fileLoader;
        scheduler.runTaskAsync(() -> {
            Pair<Long, Locale> data = fileLoader.getData(player);
            setAmount(data.getKey());
            setLocale(data.getValue());
        });
    }

    @Override
    public void savePlayer(Player player, boolean async) {
        if (async)
            CompletableFuture.runAsync(() -> fileLoader.removePlayer(player, getAmount(), getLocale()));
        else
            fileLoader.removePlayer(player, getAmount(), getLocale());
    }
}
