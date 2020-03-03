package software.bigbade.enchantmenttokens.utils;

import software.bigbade.enchantmenttokens.EnchantmentTokens;

import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnchantLogger {
    private static final Logger logger = Logger.getLogger(EnchantmentTokens.class.getCanonicalName());
    private static final Formatter formatter = new Formatter();
    //Private constructor to hide implicit public constructor
    private EnchantLogger() {}

    public static void log(Level level, String message, Object... args) {
        logger.log(level, formatter.format("[EnchantmentTokens] %s", message).toString(), args);
    }

    public static void log(String message, Throwable error) {
        logger.log(Level.SEVERE, "[EnchantmentTokens] " + message, error);
    }
}
