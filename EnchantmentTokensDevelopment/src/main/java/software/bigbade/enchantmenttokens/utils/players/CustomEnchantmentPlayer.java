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

package software.bigbade.enchantmenttokens.utils.players;

import lombok.RequiredArgsConstructor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.currency.CurrencyHandler;
import software.bigbade.enchantmenttokens.gui.EnchantmentGUI;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@RequiredArgsConstructor
public class CustomEnchantmentPlayer implements EnchantmentPlayer {
    private final Player player;
    private final CurrencyHandler handler;

    private EnchantmentGUI currentGUI;

    private long doubler = 1;

    public void save() {
        handler.savePlayer(this);
    }

    public Player getPlayer() {
        return player;
    }

    public CompletableFuture<Long> getGems() {
        return handler.getAmount();
    }

    public void addGems(long amount) {
        handler.addAmount(amount);
    }

    public EnchantmentGUI getCurrentGUI() {
        return currentGUI;
    }

    public long getDoubler() {
        return doubler;
    }

    public void removeDoubler(long removing) {
        doubler -= removing;
    }

    public void resetDoubler() {
        doubler = 1;
    }

    public void addDoubler(long adding) {
        doubler += adding;
    }

    @Override
    public <T> void storeValue(NamespacedKey namespacedKey, T t) {
        handler.storePlayerData(namespacedKey, t.toString());
    }

    @Override
    public <T> void storeValue(NamespacedKey namespacedKey, T t, Function<T, String> function) {
        handler.storePlayerData(namespacedKey, function.apply(t));
    }

    @Nullable
    @Override
    public <T> T getValue(NamespacedKey namespacedKey, Function<String, T> function) {
        return function.apply(handler.getPlayerData(namespacedKey));
    }

    public void setCurrentGUI(EnchantmentGUI currentGUI) {
        this.currentGUI = currentGUI;
    }

    @Override
    public Locale getLanguage() {
        return handler.getLocale();
    }

    @Override
    public void setLanguage(Locale language) {
        handler.setLocale(language);
    }
}
