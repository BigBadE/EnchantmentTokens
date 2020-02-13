package software.bigbade.enchantmenttokens.skript.type;

import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.util.EnchantmentType;
import org.bukkit.Bukkit;
import org.eclipse.jdt.annotation.Nullable;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.skript.SkriptEnchantment;

import java.util.concurrent.atomic.AtomicReference;

public class BaseParser extends Parser<SkriptEnchantment> {
    @Override
    @Nullable
    public SkriptEnchantment parse(final String s, final ParseContext context) {
        if (context != ParseContext.SCRIPT) return null;
        AtomicReference<SkriptEnchantment> found = new AtomicReference<>();
        ((EnchantmentTokens) Bukkit.getPluginManager().getPlugin("Enchantmenttokens")).getEnchantmentHandler().getSkriptEnchantments().forEach((base) -> {
            if (base.getName().equals(s))
                found.set(base);
        });
        return found.get();
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
