package software.bigbade.enchantmenttokens.utils;

import software.bigbade.enchantmenttokens.EnchantmentTokens;
import org.bukkit.Bukkit;

public class SchedulerHandler {
    private EnchantmentTokens main;

    public SchedulerHandler(EnchantmentTokens main) {
        this.main = main;
    }

    public void runTaskLater(Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLater(main, runnable, delay).getTaskId();
    }

    public void runTaskRepeating(Runnable runnable, long delay, long repeat) {
        Bukkit.getScheduler().runTaskTimer(main, runnable, delay, repeat).getTaskId();
    }

    public void runTaskAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(main, runnable);
    }

    public int runTaskAsyncRepeating(Runnable runnable, long delay, long repeat) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(main, runnable, delay, repeat).getTaskId();
    }
}
