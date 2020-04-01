/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.utils.currency;

import javafx.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.loader.FileLoader;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;

import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class GemCurrencyHandler extends EnchantCurrencyHandler {
    private FileLoader fileLoader;

    public GemCurrencyHandler(Player player, FileLoader fileLoader, SchedulerHandler scheduler) {
        super("gemsOld");
        this.fileLoader = fileLoader;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Pair<Long, Locale>> callable = () -> fileLoader.getData(player);
        Future<Pair<Long, Locale>> future = executor.submit(callable);
        final AtomicInteger id = new AtomicInteger();
        id.set(scheduler.runTaskAsyncRepeating(() -> {
            try {
                Pair<Long, Locale> data = future.get(1, TimeUnit.MILLISECONDS);
                setAmount(data.getKey());
                setLocale(data.getValue());
                Bukkit.getScheduler().cancelTask(id.get());
            } catch (InterruptedException e) {
                EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "gem thread interrupted", e);
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not load gems", e);
            } catch (TimeoutException ignored) {
                //Ignore timeouts
            }
        }, 1L, 1L));
        executor.shutdown();
    }

    @Override
    public void savePlayer(Player player, boolean async) {
        if (async)
            CompletableFuture.runAsync(() -> fileLoader.removePlayer(player, getAmount(), getLocale()));
        else
            fileLoader.removePlayer(player, getAmount(), getLocale());
    }
}
