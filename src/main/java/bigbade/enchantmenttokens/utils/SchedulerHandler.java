package bigbade.enchantmenttokens.utils;

import bigbade.enchantmenttokens.EnchantmentTokens;
import org.bukkit.Bukkit;

public class SchedulerHandler {
    private EnchantmentTokens main;

    public SchedulerHandler(EnchantmentTokens main) {
        this.main = main;
    }

    public int runTaskLater(Runnable runnable, long delay) {
        return Bukkit.getScheduler().runTaskLater(main, runnable, delay).getTaskId();
    }

    public int runTaskAsyncRepeating(Runnable runnable, long delay, long repeat) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(main, runnable, delay, repeat).getTaskId();
    }
}
