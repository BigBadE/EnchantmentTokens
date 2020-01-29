package bigbade.enchantmenttokens.api;

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

import bigbade.enchantmenttokens.gui.EnchantmentGUI;
import bigbade.enchantmenttokens.utils.currency.CurrencyHandler;
import org.bukkit.entity.Player;

import java.io.Serializable;

public class EnchantmentPlayer implements Serializable {
    private Player player;

    private EnchantmentGUI currentGUI;

    private CurrencyHandler handler;

    private EnchantmentPlayer(Player player) {
        this.player = player;
    }

    public static EnchantmentPlayer loadPlayer(Player player) {
        return new EnchantmentPlayer(player);
    }

    public void save() {
        handler.savePlayer(player);
    }

    public Player getPlayer() {
        return player;
    }

    public long getGems() {
        return handler.getAmount();
    }

    public void addGems(long amount) {
        handler.addAmount(amount);
    }

    public EnchantmentGUI getCurrentGUI() {
        return currentGUI;
    }

    public void setCurrentGUI(EnchantmentGUI currentGUI) {
        this.currentGUI = currentGUI;
    }

    public void setHandler(CurrencyHandler handler) {
        this.handler = handler;
    }
}
