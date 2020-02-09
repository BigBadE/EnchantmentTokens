package software.bigbade.enchantmenttokens.utils;

import software.bigbade.enchantmenttokens.EnchantmentTokens;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EnchantLogger {
    private static final Logger logger = Logger.getLogger(EnchantmentTokens.class.getCanonicalName());

    public static void log(Level level, String message, Object... args) {
        logger.log(level, "[EnchantmentTokens] " + message, args);
    }
}
