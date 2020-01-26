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
import bigbade.enchantmenttokens.gui.EnchantPickerGUI;
import bigbade.enchantmenttokens.listeners.SignClickListener;
import bigbade.enchantmenttokens.listeners.SignPacketHandler;
import bigbade.enchantmenttokens.listeners.SignPlaceListener;
import bigbade.enchantmenttokens.listeners.gui.EnchantmentGUIListener;
import bigbade.enchantmenttokens.loader.FileLoader;
import bigbade.enchantmenttokens.utils.ConfigurationManager;
import bigbade.enchantmenttokens.utils.EnchantmentHandler;
import bigbade.enchantmenttokens.utils.ListenerHandler;
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
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;

public class EnchantmentTokens extends JavaPlugin {
    public Map<String, Set<Class<EnchantmentBase>>> enchants;

    private EnchantPickerGUI enchantPickerGUI;
    public static Logger LOGGER;

    private EnchantMenuCmd menuCmd;
    public FileLoader fileLoader;

    public EnchantmentLoader loader;

    public SignPacketHandler signHandler;

    private int version;

    private EnchantmentHandler enchantmentHandler;
    private ListenerHandler listenerHandler;

    @Override
    public void onEnable() {
        LOGGER = getLogger();
        getConfig().options().copyHeader(true).header("#Add all vanilla enchantments used in here!\nCheck configurationguide.txt for names/versions.");
        saveDefaultConfig();
        version = Integer.parseInt(Bukkit.getVersion().split("\\.")[1]);

        if (!getConfig().isSet("metrics"))
            getConfig().set("metrics", true);

        boolean metrics = getConfig().getBoolean("metics");

        if (metrics) {
            Metrics bstats = new Metrics(this, 6283);
        }

        ConfigurationManager.saveConfigurationGuide(this, getDataFolder().getPath() + "/configurationguide.txt", getClassLoader());

        fileLoader = new FileLoader(this);
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

        if (Bukkit.getPluginManager().isPluginEnabled(this)) {
            enchantPickerGUI = new EnchantPickerGUI(this);
            registerCommands();
            registerListeners();
        }

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        getLogger().info("Registering sign listener");
        signHandler = new SignPacketHandler(protocolManager, this, enchantmentHandler.getEnchantments());

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
        fileLoader.saveCache();
        saveConfig();
        loader.getAddons().forEach(EnchantmentAddon::onDisable);
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new ArmorListener(new ArrayList<>()), this);
        Bukkit.getPluginManager().registerEvents(new DispenserArmorListener(), this);

        Bukkit.getPluginManager().registerEvents(new SignPlaceListener(this), this);
        Bukkit.getPluginManager().registerEvents(new SignClickListener(this), this);

        Bukkit.getPluginManager().registerEvents(new EnchantmentGUIListener(this, enchantPickerGUI, menuCmd, version), this);

        listenerHandler.registerListeners();
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("adminenchant")).setExecutor(new EnchantCmd(this));
        Objects.requireNonNull(getCommand("adminenchant")).setTabCompleter(new EnchantTabCompleter(this));

        Objects.requireNonNull(getCommand("addgems")).setExecutor(new AddGemCmd(this));
        Objects.requireNonNull(getCommand("addgems")).setTabCompleter(new AddGemTabCompleter());

        menuCmd = new EnchantMenuCmd(version);
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

    public Map<EnchantmentBase, Method> getListeners(ListenerType type) {
        return listenerHandler.getEnchantListeners().get(type);
    }

    public List<EnchantmentBase> getEnchantments() {
        return enchantmentHandler.getEnchantments();
    }

    public List<VanillaEnchant> getVanillaEnchantments() {
        return enchantmentHandler.getVanillaEnchants();
    }

    public void registerEnchants() {
        loader = new EnchantmentLoader(new File(getDataFolder().getPath() + "\\enchantments"), getLogger());
        enchants = loader.getEnchantments();
        enchantmentHandler.registerEnchants(listenerHandler.loadEnchantments(enchants));
    }
}