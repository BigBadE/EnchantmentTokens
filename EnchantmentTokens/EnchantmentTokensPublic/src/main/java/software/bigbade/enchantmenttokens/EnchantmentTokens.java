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

package software.bigbade.enchantmenttokens;

import org.bukkit.plugin.java.JavaPlugin;
import software.bigbade.enchantmenttokens.currency.CurrencyFactory;
import software.bigbade.enchantmenttokens.gui.MenuFactory;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;
import software.bigbade.enchantmenttokens.utils.SignHandler;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantUtils;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerHandler;
import software.bigbade.enchantmenttokens.utils.players.PlayerHandler;

import java.io.File;
import java.util.logging.Logger;

public abstract class EnchantmentTokens extends JavaPlugin {
    //Name used for NamespacedKey namespaces
    public static final String NAME = "enchantmenttokens";

    private static Logger logger;

    public abstract void unregisterEnchants();

    public abstract void registerEnchants();

    public void setLogger(Logger logger) {
        setEnchantLogger(logger);
    }

    private static void setEnchantLogger(Logger logger) {
        EnchantmentTokens.logger = logger;
    }

    public static Logger getEnchantLogger() {
        return logger;
    }

    public abstract EnchantmentHandler getEnchantmentHandler();

    public abstract ListenerHandler getListenerHandler();

    public abstract PlayerHandler getPlayerHandler();

    public abstract CurrencyFactory getCurrencyHandler();

    public abstract int getVersion();

    public abstract SignHandler getSignHandler();

    public abstract EnchantUtils getUtils();

    public abstract MenuFactory getMenuFactory();

    public abstract SchedulerHandler getScheduler();

    public abstract File getEnchantmentFolder();
}