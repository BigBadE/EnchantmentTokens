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
import bigbade.enchantmenttokens.listeners.*;
import bigbade.enchantmenttokens.listeners.gui.EnchantmentGUIListener;
import bigbade.enchantmenttokens.utils.ConfigurationManager;
import bigbade.enchantmenttokens.utils.EnchantmentHandler;
import bigbade.enchantmenttokens.utils.EnchantmentPlayerHandler;
import bigbade.enchantmenttokens.utils.ListenerHandler;
import bigbade.enchantmenttokens.utils.currency.CurrencyHandler;
import bigbade.enchantmenttokens.utils.currency.GemCurrencyHandler;
import bigbade.enchantmenttokens.utils.currency.LatestCurrencyHandler;
import com.codingforcookies.armorequip.ArmorListener;
import com.codingforcookies.armorequip.DispenserArmorListener;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class EnchantmentTokens extends JavaPlugin {
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
            getConfig().set("currency", "gems");

        playerHandler = new EnchantmentPlayerHandler();

        String currency = getConfig().getString("currency");

        if ("gems".equalsIgnoreCase(currency)) {
            if (version >= 14) {
                if (!getConfig().isSet("usePersistentData"))
                    getConfig().set("usePersistentData", true);
                if (getConfig().getBoolean("usePersistentData"))
                    currencyHandler = new LatestCurrencyHandler(this);
                else
                    currencyHandler = new GemCurrencyHandler(this);
            }
            else
                currencyHandler = new GemCurrencyHandler(this);
        }

        boolean metrics = getConfig().getBoolean("metics");

        if (metrics) {
            new Metrics(this, 6283);
        }

        ConfigurationManager.saveConfigurationGuide(this, getDataFolder().getPath() + "/configurationguide.txt", getClassLoader());

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
        signHandler = new SignPacketHandler(protocolManager, this, enchantmentHandler.getEnchantments());

        if (Bukkit.getPluginManager().isPluginEnabled(this)) {
            enchantmentPickerManager = new EnchantmentPickerManager(this);
            registerCommands();
            registerListeners();
        }

        for (EnchantmentAddon addon : loader.getAddons()) {
            FileConfiguration configuration = ConfigurationManager.loadConfigurationFile(getDataFolder().getPath() + "\\enchantments\\" + addon.getName() + ".yml");

            for (Field field : addon.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(ConfigurationField.class)) {
                    ConfigurationManager.loadConfigForField(field, configuration, addon);
                }
            }
        }
        loader.getAddons().forEach(EnchantmentAddon::onEnable);
    }

    @Override
    public void onDisable() {
        saveConfig();
        loader.getAddons().forEach(EnchantmentAddon::onDisable);
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new ArmorListener(new ArrayList<>()), this);
        Bukkit.getPluginManager().registerEvents(new DispenserArmorListener(), this);

        Bukkit.getPluginManager().registerEvents(new SignPlaceListener(this), this);
        Bukkit.getPluginManager().registerEvents(new SignClickListener(this), this);

        Bukkit.getPluginManager().registerEvents(new EnchantmentGUIListener(this, enchantmentPickerManager, menuCmd, version), this);

        Bukkit.getPluginManager().registerEvents(new ChunkUnloadListener(signHandler.getSigns()), this);
        Bukkit.getPluginManager().registerEvents(new PlayerLeaveListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(playerHandler, currencyHandler), this);
        listenerHandler.registerListeners();
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("adminenchant")).setExecutor(new EnchantCmd(this));
        Objects.requireNonNull(getCommand("adminenchant")).setTabCompleter(new EnchantTabCompleter(this));

        Objects.requireNonNull(getCommand("addgems")).setExecutor(new AddGemCmd(this));
        Objects.requireNonNull(getCommand("addgems")).setTabCompleter(new AddGemTabCompleter());

        menuCmd = new EnchantMenuCmd(version, this);
        Objects.requireNonNull(getCommand("tokenenchant")).setExecutor(menuCmd);
        Objects.requireNonNull(getCommand("tokenenchant")).setTabCompleter(new GenericTabCompleter());

        Objects.requireNonNull(getCommand("gembal")).setExecutor(new BalanceCmd(this));
        Objects.requireNonNull(getCommand("gembal")).setTabCompleter(new GenericTabCompleter());

        Objects.requireNonNull(getCommand("enchantlist")).setExecutor(new EnchantmentList(this));
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
        loader = new EnchantmentLoader(new File(getDataFolder().getPath() + "\\enchantments"), getLogger());
        enchantmentHandler.registerEnchants(listenerHandler.loadEnchantments(loader.getEnchantments()));
    }

    public EnchantmentPlayerHandler getPlayerHandler() {
        return playerHandler;
    }

    public CurrencyHandler getCurrencyHandler() {
        return currencyHandler;
    }
}