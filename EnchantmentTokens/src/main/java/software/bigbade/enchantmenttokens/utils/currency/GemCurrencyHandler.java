package software.bigbade.enchantmenttokens.utils.currency;

import software.bigbade.enchantmenttokens.loader.FileLoader;
import software.bigbade.enchantmenttokens.utils.EnchantLogger;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class GemCurrencyHandler implements CurrencyHandler {
    private long gems = -1;
    private FileLoader fileLoader;

    public GemCurrencyHandler(Player player, FileLoader fileLoader, SchedulerHandler scheduler) {
        this.fileLoader = fileLoader;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Long> callable = () -> fileLoader.getGems(player);
        Future<Long> future = executor.submit(callable);
        final AtomicInteger id = new AtomicInteger();
        id.set(scheduler.runTaskAsyncRepeating(() -> {
            try {
                gems = future.get(1, TimeUnit.MILLISECONDS);
                Bukkit.getScheduler().cancelTask(id.get());
            } catch (InterruptedException e) {
                EnchantLogger.log(Level.SEVERE, "gem thread interrupted", e);
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                EnchantLogger.log(Level.SEVERE, "Could not load gems", e);
            } catch (TimeoutException ignored) {
                EnchantLogger.log(Level.INFO, "Reading gems took longer than 1 tick");
            }
        }, 1L, 1L));
        executor.shutdown();
    }

    @Override
    public long getAmount() {
        return gems;
    }

    @Override
    public void setAmount(long amount) {
        gems = amount;
    }

    @Override
    public void addAmount(long amount) {
        gems += amount;
    }

    @Override
    public void savePlayer(Player player) {
        CompletableFuture.runAsync(() -> fileLoader.removePlayer(player, gems));
    }

    @Override
    public String name() {
        return "gemsOld";
    }
}
