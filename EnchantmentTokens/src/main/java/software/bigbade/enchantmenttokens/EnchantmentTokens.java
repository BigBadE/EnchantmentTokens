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
import software.bigbade.enchantmenttokens.commands.CommandManager;
import software.bigbade.enchantmenttokens.gui.EnchantmentMenuFactory;
import software.bigbade.enchantmenttokens.gui.MenuFactory;
import software.bigbade.enchantmenttokens.listeners.SignPacketHandler;
import software.bigbade.enchantmenttokens.localization.LocaleManager;
import software.bigbade.enchantmenttokens.skript.type.SkriptManager;
import software.bigbade.enchantmenttokens.utils.EnchantLogger;
import software.bigbade.enchantmenttokens.utils.MetricManager;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;
import software.bigbade.enchantmenttokens.utils.configuration.ConfigurationManager;
import software.bigbade.enchantmenttokens.utils.configuration.ConfigurationType;
import software.bigbade.enchantmenttokens.utils.currency.CurrencyFactory;
import software.bigbade.enchantmenttokens.utils.currency.CurrencyFactoryHandler;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantUtils;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentLoader;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerHandler;
import software.bigbade.enchantmenttokens.utils.players.EnchantmentPlayerHandler;

import java.io.File;
import java.util.logging.Level;

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
        version = Integer.parseInt(Bukkit.getVersion().split("\\.")[1]);
        scheduler = new SchedulerHandler(this);

        setupConfiguration();

        setupCurrency();

        setupProtocolManager();

        registerEnchants();

        setupSkript();

        LocaleManager.updateLocale(getConfig(), loader.getAddons());

        utils = new EnchantUtils(enchantmentHandler, playerHandler, listenerHandler, signHandler.getSigns());

        factory = new EnchantmentMenuFactory(version, playerHandler, utils, enchantmentHandler);

        CommandManager.registerCommands(this);

        setupAutosave();

        EnchantLogger.log(Level.INFO, "Successfully enabled EnchantmentTokens");

        saveConfig();
    }

    private void setupProtocolManager() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        signHandler = new SignPacketHandler(protocolManager, this, new ConfigurationType<>("gems").getValue("type", ConfigurationManager.getSectionOrCreate(getConfig(), "currency")).equalsIgnoreCase("vault"));
    }

    private void setupSkript() {
        if (Bukkit.getPluginManager().isPluginEnabled("Skript")) {
            SkriptAddon addon = Skript.registerAddon(this);
            new SkriptManager(addon);
        }
    }

    private void setupCurrency() {
        ConfigurationSection currency = ConfigurationManager.getSectionOrCreate(getConfig(), "currency");

        CurrencyFactoryHandler handler = new CurrencyFactoryHandler(getDataFolder().getAbsolutePath(), scheduler, currency, version);
        currencyFactory = handler.load();

        playerHandler = new EnchantmentPlayerHandler(currencyFactory);
    }

    private void setupAutosave() {
        int autosaveTime = new ConfigurationType<>(15).getValue("autosaveTime", getConfig());

        saveConfig();

        autosaveTime *= 20 * 60;

        Bukkit.getScheduler().runTaskTimer(this, () -> playerHandler.autosave(scheduler), autosaveTime, autosaveTime);
    }

    private void setupConfiguration() {
        getConfig().options().copyHeader(true);
        saveDefaultConfig();
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
        loader.getAddons().forEach(EnchantmentAddon::onDisable);
        enchantmentHandler.getEnchantments().forEach(EnchantmentBase::onDisable);
        playerHandler.shutdown();
        currencyFactory.shutdown();
        saveConfig();
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
}