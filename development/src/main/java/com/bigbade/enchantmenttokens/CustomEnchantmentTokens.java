/*
 * Custom enchantments for Minecraft
 * Copyright (C) 2021 Big_Bad_E
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

package com.bigbade.enchantmenttokens;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import com.bigbade.enchantmenttokens.api.CustomEnchantmentLoader;
import com.bigbade.enchantmenttokens.api.CustomStandaloneEnchantHandler;
import com.bigbade.enchantmenttokens.api.EnchantmentBase;
import com.bigbade.enchantmenttokens.api.StandaloneEnchantHandler;
import com.bigbade.enchantmenttokens.commands.CommandManager;
import com.bigbade.enchantmenttokens.configuration.ConfigurationManager;
import com.bigbade.enchantmenttokens.configuration.ConfigurationType;
import com.bigbade.enchantmenttokens.currency.CurrencyFactory;
import com.bigbade.enchantmenttokens.gui.CustomButtonFactory;
import com.bigbade.enchantmenttokens.gui.CustomMenuFactory;
import com.bigbade.enchantmenttokens.gui.EnchantmentMenuFactory;
import com.bigbade.enchantmenttokens.listeners.packet.EnchantmentTablePacketHandler;
import com.bigbade.enchantmenttokens.listeners.packet.SignPacketHandler;
import com.bigbade.enchantmenttokens.localization.LocaleManager;
import com.bigbade.enchantmenttokens.skript.type.SkriptManager;
import com.bigbade.enchantmenttokens.utils.ButtonFactory;
import com.bigbade.enchantmenttokens.utils.MetricManager;
import com.bigbade.enchantmenttokens.utils.SchedulerHandler;
import com.bigbade.enchantmenttokens.utils.currency.CurrencyAdditionHandler;
import com.bigbade.enchantmenttokens.utils.currency.CurrencyFactoryHandler;
import com.bigbade.enchantmenttokens.utils.currency.ExperienceCurrencyFactory;
import com.bigbade.enchantmenttokens.utils.currency.VaultCurrencyFactory;
import com.bigbade.enchantmenttokens.utils.enchants.CustomEnchantUtils;
import com.bigbade.enchantmenttokens.utils.enchants.CustomEnchantmentHandler;
import com.bigbade.enchantmenttokens.utils.enchants.EnchantUtils;
import com.bigbade.enchantmenttokens.utils.enchants.EnchantmentFileLoader;
import com.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import com.bigbade.enchantmenttokens.utils.enchants.EnchantmentLoader;
import com.bigbade.enchantmenttokens.utils.listeners.EnchantListenerHandler;
import com.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;

import java.io.File;
import java.util.logging.Level;

public class CustomEnchantmentTokens extends EnchantmentTokens {
    @Getter
    private final File enchantmentFolder = new File(getDataFolder().getPath() + "\\enchantments");
    @Getter
    private final EnchantListenerHandler listenerHandler = new EnchantListenerHandler(this);
    @Getter
    private final SchedulerHandler scheduler = new SchedulerHandler(this);
    @Getter
    private final EnchantmentLoader enchantmentLoader = new CustomEnchantmentLoader(this);

    @Getter
    private EnchantmentHandler enchantmentHandler;
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
    private boolean overridingEnchantTables = false;

    private EnchantmentFileLoader loader;

    /**
     * Everything is set up here
     */
    @Override
    public void onEnable() {
        setEnchantLogger(getLogger());
        enchantmentHandler = new CustomEnchantmentHandler(getConfig(), getDataFolder().getAbsolutePath()
                + "\\skript.yml");
        ButtonFactory.setInstance(new CustomButtonFactory());
        //Sets static fields like the empty button
        setup(this, LocaleManager.getLocale(getConfig()));
        saveDefaultConfig();

        setupConfiguration();
        setupCurrency();
        setupProtocolManager();

        //Must be setup after sign handler
        utils = new CustomEnchantUtils(listenerHandler, signHandler.getSigns());
        //Needs to be after utils
        menuFactory = new CustomMenuFactory(playerHandler, utils, enchantmentHandler);

        //Must be called after the menu factory is set, so all registered addons can add buttons
        StandaloneEnchantHandler.setInstance(new CustomStandaloneEnchantHandler(enchantmentLoader));
        registerEnchants();

        setupSkript();

        CommandManager.registerCommands(this);
        setupAutosave();
    }

    private void setupProtocolManager() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        signHandler = new SignPacketHandler(protocolManager, this);
        overridingEnchantTables = new ConfigurationType<>(false)
                .getValue("override-enchantment-table", getConfig());
        if (overridingEnchantTables) {
            EnchantmentTokens.getEnchantLogger().log(Level.INFO,
                    "Enchantment Table overriding is enabled, this is a BETA feature, be careful!");
            protocolManager.addPacketListener(new EnchantmentTablePacketHandler(this));
        }
    }

    private void setupSkript() {
        if (Bukkit.getPluginManager().isPluginEnabled("Skript")) {
            SkriptAddon addon = Skript.registerAddon(this).setLanguageFileDirectory("lang");
            new SkriptManager(addon);
        }
    }

    private void setupCurrency() {
        CurrencyFactoryHandler handler = new CurrencyFactoryHandler(this);
        currencyHandler = handler.load();

        CurrencyAdditionHandler.initialize(!(currencyHandler instanceof VaultCurrencyFactory)
                        && !(currencyHandler instanceof ExperienceCurrencyFactory),
                currencyHandler instanceof ExperienceCurrencyFactory);

        playerHandler = new EnchantmentPlayerHandler(currencyHandler);
    }

    private void setupAutosave() {
        int autosaveTime = new ConfigurationType<>(15).getValue("autosaveTime", getConfig());

        autosaveTime *= 20 * 60;

        Bukkit.getScheduler().runTaskTimer(this,
                () -> playerHandler.autosave(scheduler), autosaveTime, autosaveTime);
    }

    private void setupConfiguration() {
        boolean metrics = new ConfigurationType<>(true).getValue("metrics", getConfig());

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
        if (loader != null) {
            loader.getAddons().forEach(Plugin::onDisable);
        }
        enchantmentHandler.getAllEnchants().forEach(EnchantmentBase::onDisable);
        playerHandler.shutdown();
        currencyHandler.shutdown();
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
