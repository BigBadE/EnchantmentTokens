/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.commands.CommandManager;
import software.bigbade.enchantmenttokens.configuration.ConfigurationManager;
import software.bigbade.enchantmenttokens.configuration.ConfigurationType;
import software.bigbade.enchantmenttokens.currency.CurrencyFactory;
import software.bigbade.enchantmenttokens.gui.EnchantmentMenuFactory;
import software.bigbade.enchantmenttokens.gui.MenuFactory;
import software.bigbade.enchantmenttokens.listeners.SignPacketHandler;
import software.bigbade.enchantmenttokens.localization.LocaleManager;
import software.bigbade.enchantmenttokens.skript.type.SkriptManager;
import software.bigbade.enchantmenttokens.utils.MetricManager;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;
import software.bigbade.enchantmenttokens.utils.SignHandler;
import software.bigbade.enchantmenttokens.utils.currency.CurrencyAdditionHandler;
import software.bigbade.enchantmenttokens.utils.currency.CurrencyFactoryHandler;
import software.bigbade.enchantmenttokens.utils.currency.ExperienceCurrencyFactory;
import software.bigbade.enchantmenttokens.utils.currency.VaultCurrencyFactory;
import software.bigbade.enchantmenttokens.utils.enchants.CustomEnchantUtils;
import software.bigbade.enchantmenttokens.utils.enchants.CustomEnchantmentHandler;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantUtils;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentLoader;
import software.bigbade.enchantmenttokens.utils.listeners.EnchantListenerHandler;
import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;
import software.bigbade.enchantmenttokens.utils.players.PlayerHandler;

import java.io.File;
import java.util.logging.Level;

public class CustomEnchantmentTokens extends EnchantmentTokens {
    private File enchantmentFolder = new File(getDataFolder().getPath() + "\\enchantments");

    private EnchantmentLoader loader;

    private SignPacketHandler signHandler;

    //Minecraft version
    private int version;

    private EnchantmentHandler enchantmentHandler;
    private EnchantListenerHandler listenerHandler;

    private CurrencyFactory currencyFactory;

    private EnchantmentPlayerHandler playerHandler;

    private EnchantUtils utils;

    private MenuFactory factory;

    private SchedulerHandler scheduler;

    /**
     * Everything is set up here
     */
    @Override
    public void onEnable() {
        setLogger(getLogger());
        version = Integer.parseInt(Bukkit.getVersion().split("\\.")[1]);
        scheduler = new SchedulerHandler(this);

        setupConfiguration();

        setupCurrency();

        setupProtocolManager();

        registerEnchants();

        setupSkript();

        LocaleManager.updateLocale(getConfig(), loader.getAddons());

        utils = new CustomEnchantUtils(enchantmentHandler, playerHandler, listenerHandler, signHandler.getSigns());

        factory = new EnchantmentMenuFactory(version, playerHandler, utils, enchantmentHandler);

        CommandManager.registerCommands(this);

        setupAutosave();

        EnchantmentTokens.getEnchantLogger().log(Level.INFO, "Successfully enabled EnchantmentTokens");

        saveConfig();
    }

    private void setupProtocolManager() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        signHandler = new SignPacketHandler(protocolManager, this);
    }

    private void setupSkript() {
        if (Bukkit.getPluginManager().isPluginEnabled("Skript")) {
            SkriptAddon addon = Skript.registerAddon(this);
            new SkriptManager(addon);
        }
    }

    private void setupCurrency() {
        CurrencyFactoryHandler handler = new CurrencyFactoryHandler(this);
        currencyFactory = handler.load();

        CurrencyAdditionHandler.initialize(!(currencyFactory instanceof VaultCurrencyFactory) && !(currencyFactory instanceof ExperienceCurrencyFactory), currencyFactory instanceof ExperienceCurrencyFactory);

        playerHandler = new EnchantmentPlayerHandler(currencyFactory);
    }

    private void setupAutosave() {
        int autosaveTime = new ConfigurationType<>(15).getValue("autosaveTime", getConfig());

        autosaveTime *= 20 * 60;

        Bukkit.getScheduler().runTaskTimer(this, () -> playerHandler.autosave(scheduler), autosaveTime, autosaveTime);
    }

    private void setupConfiguration() {
        getConfig().options().copyHeader(true);
        saveDefaultConfig();
        boolean metrics = new ConfigurationType<>(true).getValue("metrics", getConfig());

        saveConfig();

        if (metrics) {
            new MetricManager(this);
        }

        ConfigurationManager.saveConfigurationGuide(scheduler, getDataFolder().getPath());
        ConfigurationManager.createFolder(getDataFolder());
        ConfigurationManager.createFolder(getDataFolder().getPath() + "\\data");
        ConfigurationManager.createFolder(getDataFolder().getPath() + "\\enchantments");
        ConfigurationManager.createFolder(getDataFolder().getPath() + "\\storage");
    }

    @Override
    public void onDisable() {
        loader.getAddons().forEach(Plugin::onDisable);
        enchantmentHandler.getAllEnchants().forEach(EnchantmentBase::onDisable);
        playerHandler.shutdown();
        currencyFactory.shutdown();
        saveConfig();
    }

    @Override
    public void unregisterEnchants() {
        enchantmentHandler.unregisterEnchants();
    }

    @Override
    public void registerEnchants() {
        listenerHandler = new EnchantListenerHandler(this);
        enchantmentHandler = new CustomEnchantmentHandler(getConfig(), getDataFolder().getAbsolutePath() + "\\skript.yml");
        loader = new EnchantmentLoader(enchantmentFolder, this);
    }

    @Override
    public EnchantmentHandler getEnchantmentHandler() {
        return enchantmentHandler;
    }

    @Override
    public EnchantListenerHandler getListenerHandler() {
        return listenerHandler;
    }

    @Override
    public PlayerHandler getPlayerHandler() {
        return playerHandler;
    }

    @Override
    public CurrencyFactory getCurrencyHandler() {
        return currencyFactory;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public SignHandler getSignHandler() {
        return signHandler;
    }

    @Override
    public EnchantUtils getUtils() {
        return utils;
    }

    @Override
    public MenuFactory getMenuFactory() {
        return factory;
    }

    @Override
    public SchedulerHandler getScheduler() {
        return scheduler;
    }

    @Override
    public File getEnchantmentFolder() { return enchantmentFolder; }
}