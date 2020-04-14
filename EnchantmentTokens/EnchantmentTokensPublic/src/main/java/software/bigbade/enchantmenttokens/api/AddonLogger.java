package software.bigbade.enchantmenttokens.api;

import org.bukkit.plugin.Plugin;
import software.bigbade.enchantmenttokens.EnchantmentTokens;

import javax.annotation.Nonnull;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class AddonLogger extends Logger {
    private String pluginName;

    public AddonLogger(@Nonnull Plugin addon) {
        super(addon.getClass().getCanonicalName(), null);
        String prefix = addon.getDescription().getPrefix();
        this.pluginName = prefix != null ? "[" + EnchantmentTokens.NAME + "-" + prefix + "] " : "[" + EnchantmentTokens.NAME + "-" + addon.getDescription().getName() + "] ";
        this.setParent(EnchantmentTokens.getEnchantLogger());
        this.setLevel(Level.ALL);
    }

    public void log(@Nonnull LogRecord logRecord) {
        logRecord.setMessage(this.pluginName + logRecord.getMessage());
        super.log(logRecord);
    }
}
