package bigbade.enchantmenttokens.utils.currency;

import bigbade.enchantmenttokens.EnchantmentTokens;
import bigbade.enchantmenttokens.loader.FileLoader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.*;

/*
EnchantmentTokens
Copyright (C) 2019-2020 Big_Bad_E
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

public class GemCurrencyHandler implements CurrencyHandler {
    private long gems = -1;
    private FileLoader fileLoader;
    private EnchantmentTokens main;

    public GemCurrencyHandler(EnchantmentTokens main) {
        fileLoader = new FileLoader(main);
        this.main = main;
    }

    public GemCurrencyHandler(Player player, FileLoader fileLoader, EnchantmentTokens main) {
        this.fileLoader = fileLoader;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Long> callable = () -> fileLoader.getGems(player);
        Future<Long> future = executor.submit(callable);
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            try {
                gems = future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
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
    public CurrencyHandler newInstance(Player player) {
        return new GemCurrencyHandler(player, fileLoader, main);
    }

    @Override
    public void savePlayer(Player player) {
        CompletableFuture.runAsync(() -> fileLoader.removePlayer(player));
    }

    @Override
    public String name() {
        return "gemsOld";
    }
}
