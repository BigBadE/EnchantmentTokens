package software.bigbade.enchantmenttokens;

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

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import software.bigbade.enchantmenttokens.api.EnchantmentAddon;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.ExternalCurrencyData;
import software.bigbade.enchantmenttokens.gui.EnchantmentMenuFactory;
import software.bigbade.enchantmenttokens.listeners.SignPacketHandler;
import software.bigbade.enchantmenttokens.localization.LocaleManager;
import software.bigbade.enchantmenttokens.utils.ConfigurationManager;
import software.bigbade.enchantmenttokens.utils.EnchantLogger;
import software.bigbade.enchantmenttokens.utils.MetricManager;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;
import software.bigbade.enchantmenttokens.utils.commands.CommandManager;
import software.bigbade.enchantmenttokens.utils.currency.CurrencyFactory;
import software.bigbade.enchantmenttokens.utils.currency.CurrencyFactoryHandler;
import software.bigbade.enchantmenttokens.utils.currency.GemCurrencyFactory;
import software.bigbade.enchantmenttokens.utils.currency.LatestCurrencyFactory;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantUtils;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentLoader;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerHandler;
import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Objects;
import java.util.jar.JarFile;
import java.util.logging.Level;

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

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        EnchantLogger.LOGGER.info("Registering sign listener");
        signHandler = new SignPacketHandler(protocolManager, this, Objects.requireNonNull(getConfig().getString("currency")).equalsIgnoreCase("gems"));

        listenerHandler = new ListenerHandler(this);
        enchantmentHandler = new EnchantmentHandler(this);

        registerEnchants();

        ConfigurationSection currency = ConfigurationManager.getSectionOrCreate(getConfig(), "currency");

        CurrencyFactoryHandler handler = new CurrencyFactoryHandler();
        currencyFactory = handler.load(this, currency, version);

        Locale locale = Locale.US;
        String language = (String) ConfigurationManager.getValueOrDefault("country-language", getConfig(), "US");

        for (Locale foundLocale : Locale.getAvailableLocales())
            if (foundLocale.getDisplayCountry().equals(language))
                locale = foundLocale;

        LocaleManager.updateLocale(locale, loader.getAddons());

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
        currencyFactory.shutdown();
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

    public CurrencyFactory getCurrencyHandler() {
        return currencyFactory;
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

    public EnchantmentLoader getLoader() { return loader; }
}