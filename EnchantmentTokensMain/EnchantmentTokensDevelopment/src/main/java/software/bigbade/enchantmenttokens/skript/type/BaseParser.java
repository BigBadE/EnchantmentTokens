/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.skript.type;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.util.EnchantmentType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.skript.SkriptEnchantment;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class BaseParser extends Parser<SkriptEnchantment> {
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-z0-9/._-]+");

    private final EnchantmentTokens main;

    public BaseParser() {
        main = (EnchantmentTokens) Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("EnchantmentTokens"));
    }

    @Override
    @Nullable
    public SkriptEnchantment parse(@Nonnull final String name, @Nonnull final ParseContext context) {
        EnchantmentHandler enchantmentHandler = main.getEnchantmentHandler();
        for(EnchantmentBase enchantment : enchantmentHandler.getSkriptEnchant()) {
            if(enchantment.getKey().getKey().equals(name)) {
                return (SkriptEnchantment) enchantment;
            }
        }

        if(!NAME_PATTERN.matcher(name.toLowerCase()).matches()) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "{0} has invalid characters, must have a-z, 0-9, and some symbols(._-]+)", name);
            return null;
        }

        for(EnchantmentBase enchantment : enchantmentHandler.getSkriptEnchant())
            if (enchantment.getKey().getKey().equalsIgnoreCase(name))
                return (SkriptEnchantment) enchantment;
        SkriptEnchantment enchantment = new SkriptEnchantment(new NamespacedKey(Skript.getInstance(), name), name, Material.BEDROCK);
        enchantmentHandler.addSkriptEnchant(enchantment);
        return enchantment;
    }

    @Override
    @Nonnull
    public String toString(@Nonnull final SkriptEnchantment enchantment, final int flags) {
        return EnchantmentType.toString(enchantment, flags);
    }

    @Override
    @Nonnull
    public String toVariableNameString(final SkriptEnchantment e) {
        return "" + e.getKey().getKey();
    }

    @Override
    @Nonnull
    public String getVariableNamePattern() {
        return "._-]+";
    }

    @Override
    public boolean canParse(@Nonnull ParseContext context) {
        return context != ParseContext.CONFIG;
    }
}
