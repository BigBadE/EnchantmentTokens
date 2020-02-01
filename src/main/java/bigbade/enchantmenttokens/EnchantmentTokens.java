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

import bigbade.enchantmenttokens.api.*;
import bigbade.enchantmenttokens.commands.*;
import bigbade.enchantmenttokens.gui.EnchantmentPickerManager;
import bigbade.enchantmenttokens.listeners.SignPacketHandler;
import bigbade.enchantmenttokens.localization.TranslatedMessage;
import bigbade.enchantmenttokens.utils.ConfigurationManager;
import bigbade.enchantmenttokens.utils.EnchantmentHandler;
import bigbade.enchantmenttokens.utils.EnchantmentPlayerHandler;
import bigbade.enchantmenttokens.utils.ListenerHandler;
import bigbade.enchantmenttokens.utils.currency.CurrencyHandler;
import bigbade.enchantmenttokens.utils.currency.GemCurrencyHandler;
import bigbade.enchantmenttokens.utils.currency.LatestCurrencyHandler;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;

public class EnchantmentTokens extends JavaPlugin {
    public static final String NAME = "enchantmenttokens";

    private EnchantmentPickerManager enchantmentPickerManager;
    public static Logger LOGGER;

    private EnchantMenuCmd menuCmd;

    public EnchantmentLoader loader;

    public SignPacketHandler signHandler;

    //Minecraft version
    private int version;

    private EnchantmentHandler enchantmentHandler;
    private ListenerHandler listenerHandler;

    private CurrencyHandler currencyHandler;
    private EnchantmentPlayerHandler playerHandler;

    private EnchantUtils utils;

    /**
     * Everything is set up here
     */
    @Override
    public void onEnable() {
        LOGGER = getLogger();
        getConfig().options().copyHeader(true).header("#Add all vanilla enchantments used in here!\nCheck configurationguide.txt for names/versions.");
        saveDefaultConfig();
        version = Integer.parseInt(Bukkit.getVersion().split("\\.")[1]);

        if (!getConfig().isSet("metrics"))
            getConfig().set("metrics", true);

        if (!getConfig().isSet("currency"))
            getConfig().set("currency", "Gems");

        if (!getConfig().isSet("country-language"))
            getConfig().set("country-language", "US");

        Locale locale = Locale.US;
        String language = getConfig().getString("country-language");
        for (Locale foundLocale : Locale.getAvailableLocales())
            if (foundLocale.getDisplayCountry().equals(language))
                locale = foundLocale;
        updateLocale(locale);

        playerHandler = new EnchantmentPlayerHandler(this);

        String currency = getConfig().getString("currency");

        if ("gems".equalsIgnoreCase(currency)) {
            if (version >= 14) {
                if (!getConfig().isSet("usePersistentData"))
                    getConfig().set("usePersistentData", true);
                if (getConfig().getBoolean("usePersistentData"))
                    currencyHandler = new LatestCurrencyHandler(this);
                else
                    currencyHandler = new GemCurrencyHandler(this);
            } else
                currencyHandler = new GemCurrencyHandler(this);
        }

        boolean metrics = getConfig().getBoolean("metics");

        if (metrics) {
            new Metrics(this, 6283);
        }

        ConfigurationManager.saveConfigurationGuide(this, getDataFolder().getPath() + "/configurationguide.txt");

        if (!getDataFolder().exists())
            if (!getDataFolder().mkdir())
                getLogger().severe("[ERROR] COULD NOT CREATE DATA FOLDER. REPORT THIS, NOT THE NULLPOINTEREXCEPTION.");

        ConfigurationManager.createFolder(getDataFolder().getPath() + "\\data");
        getLogger().info("Looking for enchantments");

        ConfigurationManager.createFolder(getDataFolder().getPath() + "\\enchantments");

        getLogger().info("Registering enchants");

        listenerHandler = new ListenerHandler(this);
        enchantmentHandler = new EnchantmentHandler(this);

        registerEnchants();

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        getLogger().info("Registering sign listener");
        signHandler = new SignPacketHandler(protocolManager, this, Objects.requireNonNull(getConfig().getString("currency")).equalsIgnoreCase("gems"));

        utils = new EnchantUtils(enchantmentHandler, playerHandler, listenerHandler, signHandler.getSigns());

        if (Bukkit.getPluginManager().isPluginEnabled(this)) {
            enchantmentPickerManager = new EnchantmentPickerManager(enchantmentHandler, getConfig().getConfigurationSection("enchants"));
            registerCommands();
        }

        loader.getAddons().forEach(EnchantmentAddon::onEnable);

        int autosaveTime = 15;

        if (getConfig().isSet("autosaveTime"))
            autosaveTime = getConfig().getInt("autosaveTime");
        else
            getConfig().set("autosaveTime", autosaveTime);

        autosaveTime *= 20 * 60;

        Bukkit.getScheduler().runTaskTimer(this, () -> playerHandler.autosave(this), autosaveTime, autosaveTime);
    }

    public void updateLocale(Locale locale) {
        try {
            Map<String, ResourceBundle> resources = new HashMap<>();
            resources.put(NAME, new PropertyResourceBundle(getStream("messages", locale)));

            for(EnchantmentAddon addon : loader.getAddons()) {
                resources.put(addon.getName(), new PropertyResourceBundle(getStream(addon.getName(), locale)));
            }

            TranslatedMessage.updateBundles(resources);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private InputStream getStream(String name, Locale locale) {
        InputStream languageStream = EnchantmentTokens.class.getResourceAsStream(name + "-" + locale.toLanguageTag() + "_" + locale.getCountry() + ".properties");
        if(languageStream == null)
            languageStream = EnchantmentTokens.class.getResourceAsStream("messages-en_US.properties");
        return languageStream;
    }

    @Override
    public void onDisable() {
        saveConfig();
        loader.getAddons().forEach(EnchantmentAddon::onDisable);
        enchantmentHandler.getEnchantments().forEach(EnchantmentBase::onDisable);
    }

    private void registerListeners() {

    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("adminenchant")).setExecutor(new EnchantCmd(enchantmentHandler, utils));
        Objects.requireNonNull(getCommand("adminenchant")).setTabCompleter(new EnchantTabCompleter(this));

        Objects.requireNonNull(getCommand("addgems")).setExecutor(new AddGemCmd(this));
        Objects.requireNonNull(getCommand("addgems")).setTabCompleter(new AddGemTabCompleter());

        menuCmd = new EnchantMenuCmd(version, playerHandler);
        Objects.requireNonNull(getCommand("tokenenchant")).setExecutor(menuCmd);
        Objects.requireNonNull(getCommand("tokenenchant")).setTabCompleter(new GenericTabCompleter());

        Objects.requireNonNull(getCommand("gembal")).setExecutor(new BalanceCmd(this));
        Objects.requireNonNull(getCommand("gembal")).setTabCompleter(new GenericTabCompleter());

        Objects.requireNonNull(getCommand("enchantlist")).setExecutor(new EnchantmentListCommand(this));
        Objects.requireNonNull(getCommand("enchantlist")).setTabCompleter(new GenericTabCompleter());

        Objects.requireNonNull(getCommand("reloadenchants")).setExecutor(new RecompileEnchantsCmd(this));
        Objects.requireNonNull(getCommand("reloadenchants")).setTabCompleter(new GenericTabCompleter());
    }

    public void unregisterEnchants() {
        enchantmentHandler.unregisterEnchants();
    }

    public List<EnchantmentBase> getEnchantments() {
        return enchantmentHandler.getEnchantments();
    }

    public List<VanillaEnchant> getVanillaEnchantments() {
        return enchantmentHandler.getVanillaEnchants();
    }

    public void registerEnchants() {
        loader = new EnchantmentLoader(new File(getDataFolder().getPath() + "\\enchantments"), getLogger(), this);
    }

    public EnchantmentHandler getEnchantmentHandler() {
        return enchantmentHandler;
    }

    public EnchantmentPickerManager getEnchantmentPickerManager() {
        return enchantmentPickerManager;
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

    public int getVersion() { return version; }

    public SignPacketHandler getSignHandler() { return signHandler; }

    public EnchantUtils getUtils() {
        return utils;
    }
}