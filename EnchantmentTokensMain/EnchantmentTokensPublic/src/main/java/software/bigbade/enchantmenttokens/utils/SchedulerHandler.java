/*
 * Addons for the Custom Enchantment API in Minecraft
 * Copyright (C) 2020 BigBadE
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package software.bigbade.enchantmenttokens.utils;

import org.bukkit.Bukkit;
import software.bigbade.enchantmenttokens.EnchantmentTokens;

public class SchedulerHandler {
    private final EnchantmentTokens main;

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
