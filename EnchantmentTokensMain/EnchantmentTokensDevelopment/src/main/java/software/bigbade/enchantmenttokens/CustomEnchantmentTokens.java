/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import software.bigbade.enchantmenttokens.api.CustomEnchantmentLoader;
import software.bigbade.enchantmenttokens.api.CustomStandaloneEnchantHandler;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.StandaloneEnchantHandler;
import software.bigbade.enchantmenttokens.commands.CommandManager;
import software.bigbade.enchantmenttokens.configuration.ConfigurationManager;
import software.bigbade.enchantmenttokens.configuration.ConfigurationType;
import software.bigbade.enchantmenttokens.currency.CurrencyFactory;
import software.bigbade.enchantmenttokens.gui.CustomButtonFactory;
import software.bigbade.enchantmenttokens.gui.CustomMenuFactory;
import software.bigbade.enchantmenttokens.gui.EnchantmentMenuFactory;
import software.bigbade.enchantmenttokens.listeners.packet.EnchantmentTablePacketHandler;
import software.bigbade.enchantmenttokens.listeners.packet.SignPacketHandler;
import software.bigbade.enchantmenttokens.skript.type.SkriptManager;
import software.bigbade.enchantmenttokens.utils.ButtonFactory;
import software.bigbade.enchantmenttokens.utils.MetricManager;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;
import software.bigbade.enchantmenttokens.utils.currency.CurrencyAdditionHandler;
import software.bigbade.enchantmenttokens.utils.currency.CurrencyFactoryHandler;
import software.bigbade.enchantmenttokens.utils.currency.ExperienceCurrencyFactory;
import software.bigbade.enchantmenttokens.utils.currency.VaultCurrencyFactory;
import software.bigbade.enchantmenttokens.utils.enchants.CustomEnchantUtils;
import software.bigbade.enchantmenttokens.utils.enchants.CustomEnchantmentHandler;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantUtils;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentFileLoader;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentLoader;
import software.bigbade.enchantmenttokens.utils.listeners.EnchantListenerHandler;
import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;

import java.io.File;
import java.util.logging.Level;

public class CustomEnchantmentTokens extends EnchantmentTokens {
    @Getter
    private final File enchantmentFolder = new File(getDataFolder().getPath() + "\\enchantments");

    private EnchantmentFileLoader loader;

    @Getter
    private SignPacketHandler signHandler;

    @Getter
    private CurrencyFactory currencyHandler;

    @Getter
    private EnchantmentPlayerHandler playerHandler;

    @Getter
    private EnchantUtils utils;

    @Getter
    private EnchantmentMenuFactory menuFactory;

    @Getter
    private final EnchantListenerHandler listenerHandler = new EnchantListenerHandler(this);

    @Getter
    private final SchedulerHandler scheduler = new SchedulerHandler(this);

    @Getter
    private final EnchantmentHandler enchantmentHandler = new CustomEnchantmentHandler(getConfig(), getDataFolder().getAbsolutePath() + "\\skript.yml");

    @Getter
    private boolean overridingEnchantTables = false;

    @Getter
    private final EnchantmentLoader enchantmentLoader = new CustomEnchantmentLoader(this);

    /**
     * Everything is set up here
     */
    @Override
    public void onEnable() {
        setLogger(getLogger());
        ButtonFactory.setInstance(new CustomButtonFactory());
        //Sets static fields like the empty button
        EnchantmentTokens.setup(this);
        saveDefaultConfig();

        setupConfiguration();
        setupCurrency();
        setupProtocolManager();

        //Must be setup after sign handler
        utils = new CustomEnchantUtils(enchantmentHandler, playerHandler, listenerHandler, signHandler.getSigns());
        //Needs to be after utils
        menuFactory = new CustomMenuFactory(playerHandler, utils, enchantmentHandler);

        //Must be called after the menu factory is set, so all registered addons can add buttons
        StandaloneEnchantHandler.setInstance(new CustomStandaloneEnchantHandler(enchantmentLoader));
        registerEnchants();

        setupSkript();

        CommandManager.registerCommands(this);
        setupAutosave();

        EnchantmentTokens.getEnchantLogger().log(Level.INFO, "Successfully enabled EnchantmentTokens");
        //Must be done last, obviously
        saveConfig();
    }

    private void setupProtocolManager() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        signHandler = new SignPacketHandler(protocolManager, this);
        overridingEnchantTables = new ConfigurationType<>(false).getValue("override-enchantment-table", getConfig());
        if(overridingEnchantTables) {
            EnchantmentTokens.getEnchantLogger().log(Level.INFO, "Enchantment Table overriding is enabled, this is a BETA feature, be careful!");
            protocolManager.addPacketListener(new EnchantmentTablePacketHandler(this));
        }
    }

    private void setupSkript() {
        if (Bukkit.getPluginManager().isPluginEnabled("Skript")) {
            SkriptAddon addon = Skript.registerAddon(this);
            new SkriptManager(addon);
        }
    }

    private void setupCurrency() {
        CurrencyFactoryHandler handler = new CurrencyFactoryHandler(this);
        currencyHandler = handler.load();

        CurrencyAdditionHandler.initialize(!(currencyHandler instanceof VaultCurrencyFactory) && !(currencyHandler instanceof ExperienceCurrencyFactory), currencyHandler instanceof ExperienceCurrencyFactory);

        playerHandler = new EnchantmentPlayerHandler(currencyHandler);
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
        if(loader != null) {
            loader.getAddons().forEach(Plugin::onDisable);
        }
        enchantmentHandler.getAllEnchants().forEach(EnchantmentBase::onDisable);
        playerHandler.shutdown();
        currencyHandler.shutdown();
        saveConfig();
    }

    @Override
    public void unregisterEnchants() {
        enchantmentHandler.unregisterEnchants();
    }

    @Override
    public void registerEnchants() {
        loader = new EnchantmentFileLoader(enchantmentFolder, this);
    }
}