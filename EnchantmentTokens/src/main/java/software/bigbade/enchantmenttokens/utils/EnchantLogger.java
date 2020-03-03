package software.bigbade.enchantmenttokens.utils;

import software.bigbade.enchantmenttokens.EnchantmentTokens;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EnchantLogger {
    private static final Logger logger = Logger.getLogger(EnchantmentTokens.class.getCanonicalName());

    //Private constructor to hide implicit public constructor
    private EnchantLogger() {}

    public static void log(Level level, String message, Object... args) {
        logger.log(level, String.format("[EnchantmentTokens] %s", message), args);
    }

    public static void log(String message, Throwable error) {
        logger.log(Level.SEVERE, String.format("[EnchantmentTokens] %s", message), error);
    }
}
