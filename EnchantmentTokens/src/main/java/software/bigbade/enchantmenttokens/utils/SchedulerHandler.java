package software.bigbade.enchantmenttokens.utils;

import software.bigbade.enchantmenttokens.EnchantmentTokens;
import org.bukkit.Bukkit;

public class SchedulerHandler {
    private EnchantmentTokens main;

    public SchedulerHandler(EnchantmentTokens main) {
        this.main = main;
    }

    public int runTaskLater(Runnable runnable, long delay) {
        return Bukkit.getScheduler().runTaskLater(main, runnable, delay).getTaskId();
    }

    public int runTaskRepeating(Runnable runnable, long delay, long repeat) {
        return Bukkit.getScheduler().runTaskTimer(main, runnable, delay, repeat).getTaskId();
    }


    public int runTaskAsyncRepeating(Runnable runnable, long delay, long repeat) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(main, runnable, delay, repeat).getTaskId();
    }
}
