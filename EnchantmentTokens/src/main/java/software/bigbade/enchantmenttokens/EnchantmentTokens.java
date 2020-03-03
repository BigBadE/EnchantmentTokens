package software.bigbade.enchantmenttokens;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import software.bigbade.enchantmenttokens.api.EnchantmentAddon;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.gui.EnchantmentMenuFactory;
import software.bigbade.enchantmenttokens.gui.MenuFactory;
import software.bigbade.enchantmenttokens.listeners.SignPacketHandler;
import software.bigbade.enchantmenttokens.localization.LocaleManager;
import software.bigbade.enchantmenttokens.skript.type.SkriptManager;
import software.bigbade.enchantmenttokens.utils.configuration.ConfigurationManager;
import software.bigbade.enchantmenttokens.utils.MetricManager;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;
import software.bigbade.enchantmenttokens.commands.CommandManager;
import software.bigbade.enchantmenttokens.utils.configuration.ConfigurationType;
import software.bigbade.enchantmenttokens.utils.currency.CurrencyFactory;
import software.bigbade.enchantmenttokens.utils.currency.CurrencyFactoryHandler;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantUtils;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentLoader;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerHandler;
import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;

import java.io.File;
import java.util.Objects;

public class EnchantmentTokens extends JavaPlugin {
    //Name used for NamespacedKey namespaces
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

    private MenuFactory factory;

    private SchedulerHandler scheduler;

    /**
     * Everything is set up here
     */
    @Override
    public void onEnable() {
        setupConfiguration();

        version = Integer.parseInt(Bukkit.getVersion().split("\\.")[1]);

        scheduler = new SchedulerHandler(this);

        setupProtocolManager();

        registerEnchants();

        setupSkript();

        setupCurrency();

        LocaleManager.updateLocale(getConfig(), loader.getAddons());

        utils = new EnchantUtils(enchantmentHandler, playerHandler, listenerHandler, signHandler.getSigns());

        if (Bukkit.getPluginManager().isPluginEnabled(this)) {
            factory = new EnchantmentMenuFactory(this);
            new CommandManager(this);
        }

        setupAutosave();

        saveConfig();
    }

    /**
     * Allows the setting of a custom menu factory.
     *
     * @param factory A class implementing MenuFactory, called to make the /customenchantment menu
     */
    public void setMenuFactory(MenuFactory factory) {
        this.factory = factory;
    }

    private void setupProtocolManager() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        signHandler = new SignPacketHandler(protocolManager, this, Objects.requireNonNull(getConfig().getString("currency")).equalsIgnoreCase("vault"));
    }

    private void setupSkript() {
        if (Bukkit.getPluginManager().isPluginEnabled("Skript")) {
            SkriptAddon addon = Skript.registerAddon(this);
            new SkriptManager(addon);
        }
    }

    private void setupCurrency() {
        ConfigurationSection currency = ConfigurationManager.getSectionOrCreate(getConfig(), "currency");

        CurrencyFactoryHandler handler = new CurrencyFactoryHandler(this, currency, version);
        currencyFactory = handler.load();

        playerHandler = new EnchantmentPlayerHandler(currencyFactory);
    }

    private void setupAutosave() {
        int autosaveTime;
        try {
            autosaveTime = new ConfigurationType<>(15).getValue("autosaveTime", getConfig());
        } catch (ClassCastException e) {
            getConfig().set("autosaveTime", 15);
            autosaveTime = 15;
        }

        autosaveTime *= 20 * 60;

        Bukkit.getScheduler().runTaskTimer(this, () -> playerHandler.autosave(scheduler), autosaveTime, autosaveTime);
    }

    private void setupConfiguration() {
        getConfig().options().copyHeader(true).header("Add all vanilla enchantments used in here!\nCheck ConfigurationGuide.txt for names/versions.");
        saveDefaultConfig();
        boolean metrics = new ConfigurationType<>(true).getValue("metrics", getConfig());

        if (metrics) {
            new MetricManager(this);
        }

        ConfigurationManager.saveConfigurationGuide(scheduler, getDataFolder().getPath());
        ConfigurationManager.createFolder(getDataFolder());
        ConfigurationManager.createFolder(getDataFolder().getPath() + "\\data");
        ConfigurationManager.createFolder(getDataFolder().getPath() + "\\enchantments");
    }

    @Override
    public void onDisable() {
        saveConfig();
        loader.getAddons().forEach(EnchantmentAddon::onDisable);
        enchantmentHandler.getEnchantments().forEach(EnchantmentBase::onDisable);
        playerHandler.shutdown();
        currencyFactory.shutdown();
    }

    public void unregisterEnchants() {
        enchantmentHandler.unregisterEnchants();
    }

    public void registerEnchants() {
        listenerHandler = new ListenerHandler(this);
        enchantmentHandler = new EnchantmentHandler(getConfig(), getDataFolder().getAbsolutePath() + "\\skript.yml");
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

    public MenuFactory getMenuFactory() {
        return factory;
    }

    public SchedulerHandler getScheduler() {
        return scheduler;
    }

    public EnchantmentLoader getLoader() {
        return loader;
    }
}