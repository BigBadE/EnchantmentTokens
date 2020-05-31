/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.skript.type;

import ch.njol.skript.classes.Serializer;
import ch.njol.yggdrasil.Fields;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.skript.SkriptEnchantment;

import javax.annotation.Nonnull;
import java.io.StreamCorruptedException;
import java.util.Objects;

public class BaseSerializer extends Serializer<SkriptEnchantment> {
    private final EnchantmentTokens main = (EnchantmentTokens) Bukkit.getPluginManager().getPlugin("EnchantmentTokens");

    @Override
    @Nonnull
    public Fields serialize(final SkriptEnchantment enchantment) {
        Fields fields = new Fields();
        fields.putObject("key", enchantment.getKey());
        return fields;
    }

    @Override
    public boolean canBeInstantiated() {
        return false;
    }

    @Override
    public void deserialize(@Nonnull final SkriptEnchantment enchantment, @Nonnull final Fields field) {
        throw new UnsupportedOperationException("Enchantments should NOT be saved!");
    }

    @Override
    @Nonnull
    protected SkriptEnchantment deserialize(final Fields fields) throws StreamCorruptedException {
        Objects.requireNonNull(main);
        NamespacedKey key = (NamespacedKey) fields.getObject("key");
        for(EnchantmentBase enchantment : main.getEnchantmentHandler().getSkriptEnchant()) {
            if(enchantment.getKey().equals(key)) {
                return (SkriptEnchantment) enchantment;
            }
        }
        Objects.requireNonNull(key);
        SkriptEnchantment enchantment = new SkriptEnchantment(key, key.getKey(), Material.BEDROCK);
        main.getEnchantmentHandler().addSkriptEnchant(enchantment);
        return enchantment;
    }

    @Override
    public boolean mustSyncDeserialization() {
        return false;
    }
}
