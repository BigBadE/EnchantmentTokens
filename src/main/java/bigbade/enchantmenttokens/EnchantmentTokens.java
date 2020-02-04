package bigbade.enchantmenttokens;

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

import bigbade.enchantmenttokens.api.EnchantmentAddon;
import bigbade.enchantmenttokens.api.EnchantmentBase;
import bigbade.enchantmenttokens.gui.EnchantmentMenuFactory;
import bigbade.enchantmenttokens.listeners.SignPacketHandler;
import bigbade.enchantmenttokens.localization.LocaleManager;
import bigbade.enchantmenttokens.utils.ConfigurationManager;
import bigbade.enchantmenttokens.utils.EnchantLogger;
import bigbade.enchantmenttokens.utils.MetricManager;
import bigbade.enchantmenttokens.utils.SchedulerHandler;
import bigbade.enchantmenttokens.utils.commands.CommandManager;
import bigbade.enchantmenttokens.utils.currency.*;
import bigbade.enchantmenttokens.utils.enchants.EnchantUtils;
import bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import bigbade.enchantmenttokens.utils.enchants.EnchantmentLoader;
import bigbade.enchantmenttokens.utils.listeners.ListenerHandler;
import bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Locale;
import java.util.Objects;

public class EnchantmentTokens extends JavaPlugin {
    public static final String NAME = "enchantmenttokens";

    private EnchantmentLoader loader;

    private SignPacketHandler signHandler;

    //Minecraft version
    private int version;

    private EnchantmentHandler enchantmentHandler;
    private ListenerHandler listenerHandler;

    private CurrencyFactory currencyFactory;
    private EnchantmentPlayerHandler playerHandler;

    private EnchantUtils utils;

    private EnchantmentMenuFactory factory;

    private SchedulerHandler scheduler;

    /**
     * Everything is set up here
     */
    @Override
    public void onEnable() {
        getConfig().options().copyHeader(true).header("Add all vanilla enchantments used in here!\nCheck configurationguide.txt for names/versions.");
        saveDefaultConfig();
        version = Integer.parseInt(Bukkit.getVersion().split("\\.")[1]);

        scheduler = new SchedulerHandler(this);

        String currency = (String) ConfigurationManager.getValueOrDefault("currency", getConfig(), "gems");

        if ("gems".equalsIgnoreCase(currency)) {
            if (version >= 14) {
                boolean persistent = (boolean) ConfigurationManager.getValueOrDefault("usePersistentData", getConfig(), true);
                if (persistent)
                    currencyFactory = new LatestCurrencyFactory(this);
                else
                    currencyFactory = new GemCurrencyFactory(this);
            } else
                currencyFactory = new GemCurrencyFactory(this);
        }

        playerHandler = new EnchantmentPlayerHandler(currencyFactory);

        boolean metrics = (boolean) ConfigurationManager.getValueOrDefault("metrics", getConfig(), true);

        if (metrics) {
            new MetricManager(this);
        }

        ConfigurationManager.saveConfigurationGuide(this, getDataFolder().getPath() + "/configurationguide.txt");

        ConfigurationManager.createFolder(getDataFolder());

        ConfigurationManager.createFolder(getDataFolder().getPath() + "\\data");
        EnchantLogger.LOGGER.info("Looking for enchantments");

        ConfigurationManager.createFolder(getDataFolder().getPath() + "\\enchantments");

        listenerHandler = new ListenerHandler(this);
        enchantmentHandler = new EnchantmentHandler(this);

        registerEnchants();

        Locale locale = Locale.US;
        String language = (String) ConfigurationManager.getValueOrDefault("country-language", getConfig(), "US");

        for (Locale foundLocale : Locale.getAvailableLocales())
            if (foundLocale.getDisplayCountry().equals(language))
                locale = foundLocale;

        LocaleManager.updateLocale(locale, loader.getAddons());

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        EnchantLogger.LOGGER.info("Registering sign listener");
        signHandler = new SignPacketHandler(protocolManager, this, Objects.requireNonNull(getConfig().getString("currency")).equalsIgnoreCase("gems"));

        utils = new EnchantUtils(enchantmentHandler, playerHandler, listenerHandler, signHandler.getSigns());

        if (Bukkit.getPluginManager().isPluginEnabled(this)) {
            factory = new EnchantmentMenuFactory(version, playerHandler, utils, enchantmentHandler);
            new CommandManager(this);
        }

        int autosaveTime;
        try {
            autosaveTime = (Integer) ConfigurationManager.getValueOrDefault("autosaveTime", getConfig(), 15);
        } catch (ClassCastException e) {
            getConfig().set("autosaveTime", 15);
            autosaveTime = 15;
        }

        autosaveTime *= 20 * 60;

        saveConfig();

        Bukkit.getScheduler().runTaskTimer(this, () -> playerHandler.autosave(this), autosaveTime, autosaveTime);
    }

    @Override
    public void onDisable() {
        saveConfig();
        loader.getAddons().forEach(EnchantmentAddon::onDisable);
        enchantmentHandler.getEnchantments().forEach(EnchantmentBase::onDisable);
    }

    public void unregisterEnchants() {
        enchantmentHandler.unregisterEnchants();
    }

    public void registerEnchants() {
        loader = new EnchantmentLoader(new File(getDataFolder().getPath() + "\\enchantments"), this);
    }

    public EnchantmentHandler getEnchantmentHandler() {
        return enchantmentHandler;
    }

    public ListenerHandler getListenerHandler() {
        return listenerHandler;
    }

    public EnchantmentPlayerHandler getPlayerHandler() {
        return playerHandler;
    }

    public CurrencyHandler getCurrencyHandler() {
        return currencyHandler;
    }

    public int getVersion() {
        return version;
    }

    public SignPacketHandler getSignHandler() {
        return signHandler;
    }

    public EnchantUtils getUtils() {
        return utils;
    }

    public EnchantmentMenuFactory getFactory() {
        return factory;
    }

    public SchedulerHandler getScheduler() {
        return scheduler;
    }
}