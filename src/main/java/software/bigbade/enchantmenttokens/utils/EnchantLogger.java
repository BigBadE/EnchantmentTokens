package software.bigbade.enchantmenttokens.utils;

import software.bigbade.enchantmenttokens.EnchantmentTokens;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class EnchantLogger  extends Logger {
    public static final EnchantLogger LOGGER = new EnchantLogger();

    private String pluginName;

    public EnchantLogger() {
        super(EnchantmentTokens.class.getCanonicalName(), null);
        this.pluginName = "[EnchantmentTokens] ";
        this.setLevel(Level.ALL);
    }

    @Override
    public void log(@NotNull LogRecord logRecord) {
        logRecord.setMessage(this.pluginName + logRecord.getMessage());
        super.log(logRecord);
    }
}
