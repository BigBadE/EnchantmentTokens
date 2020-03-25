package software.bigbade.enchantmenttokens.skript.type;

import ch.njol.skript.classes.Serializer;
import ch.njol.yggdrasil.Fields;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.skript.SkriptEnchantment;

import java.io.StreamCorruptedException;

public class BaseSerializer extends Serializer<SkriptEnchantment> {
    private EnchantmentTokens main = (EnchantmentTokens) Bukkit.getPluginManager().getPlugin("EnchantmentTokens");

    @Override
    public Fields serialize(final SkriptEnchantment ench) {
        Fields fields = new Fields();
        fields.putObject("key", ench.getKey());
        return fields;
    }

    @Override
    public boolean canBeInstantiated() {
        return false;
    }

    @Override
    public void deserialize(final SkriptEnchantment o, final Fields f) {
        assert false;
    }

    @Override
    protected SkriptEnchantment deserialize(final Fields fields) throws StreamCorruptedException {
        NamespacedKey key = (NamespacedKey) fields.getObject("key");
        for(EnchantmentBase enchantment : main.getEnchantmentHandler().getSkriptEnchant()) {
            if(enchantment.getKey().equals(key)) {
                return (SkriptEnchantment) enchantment;
            }
        }
        SkriptEnchantment enchantment = new SkriptEnchantment(key.getKey(), Material.BEDROCK);
        main.getEnchantmentHandler().addSkriptEnchant(enchantment);
        return enchantment;
    }

    @Override
    public boolean mustSyncDeserialization() {
        return false;
    }
}
