package software.bigbade.enchantmenttokens.skript.type;

import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.util.EnchantmentType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.eclipse.jdt.annotation.Nullable;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.skript.SkriptEnchantment;
import software.bigbade.enchantmenttokens.utils.EnchantLogger;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;

import java.util.logging.Level;
import java.util.regex.Pattern;

public class BaseParser extends Parser<SkriptEnchantment> {
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-z0-9/._-]+");

    @Override
    @Nullable
    public SkriptEnchantment parse(final String name, final ParseContext context) {
        EnchantmentHandler enchantmentHandler = ((EnchantmentTokens) Bukkit.getPluginManager().getPlugin("EnchantmentTokens")).getEnchantmentHandler();
        for(SkriptEnchantment enchantment : enchantmentHandler.getSkriptEnchantments()) {
            if(enchantment.getName().equals(name)) {
                return enchantment;
            }
        }

        if(!NAME_PATTERN.matcher(name.toLowerCase()).matches()) {
            EnchantLogger.log(Level.SEVERE, "{0} has invalid characters, must have a-z (lowercase), 0-9, and some symbols(._-]+)", name);
            return null;
        }

        SkriptEnchantment enchantment = new SkriptEnchantment(name, Material.BEDROCK);
        enchantmentHandler.addSkriptEnchant(enchantment);

        return enchantment;
    }

    @Override
    public String toString(final SkriptEnchantment e, final int flags) {
        return EnchantmentType.toString(e, flags);
    }

    @Override
    public String toVariableNameString(final SkriptEnchantment e) {
        return "" + e.getName();
    }

    @Override
    public String getVariableNamePattern() {
        return ".+";
    }

    @Override
    public boolean canParse(ParseContext context) {
        return context != ParseContext.CONFIG;
    }
}
