package software.bigbade.enchantmenttokens;

import org.bukkit.plugin.java.JavaPlugin;
import software.bigbade.enchantmenttokens.currency.CurrencyFactory;
import software.bigbade.enchantmenttokens.gui.MenuFactory;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;
import software.bigbade.enchantmenttokens.utils.SignHandler;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantUtils;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerHandler;
import software.bigbade.enchantmenttokens.utils.players.PlayerHandler;

import java.io.File;
import java.util.logging.Logger;

public abstract class EnchantmentTokens extends JavaPlugin {
    //Name used for NamespacedKey namespaces
    public static final String NAME = "enchantmenttokens";

    private static Logger logger;

    public abstract void unregisterEnchants();

    public abstract void registerEnchants();

    public void setLogger(Logger logger) {
        setEnchantLogger(logger);
    }

    private static void setEnchantLogger(Logger logger) {
        EnchantmentTokens.logger = logger;
    }

    public static Logger getEnchantLogger() {
        return logger;
    }

    public abstract EnchantmentHandler getEnchantmentHandler();

    public abstract ListenerHandler getListenerHandler();

    public abstract PlayerHandler getPlayerHandler();

    public abstract CurrencyFactory getCurrencyHandler();

    public abstract int getVersion();

    public abstract SignHandler getSignHandler();

    public abstract EnchantUtils getUtils();

    public abstract MenuFactory getMenuFactory();

    public abstract SchedulerHandler getScheduler();

    public abstract File getEnchantmentFolder();
}