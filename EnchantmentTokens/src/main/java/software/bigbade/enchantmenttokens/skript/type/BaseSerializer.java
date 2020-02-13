package software.bigbade.enchantmenttokens.skript.type;

import ch.njol.skript.classes.Serializer;
import ch.njol.yggdrasil.Fields;
import org.bukkit.Bukkit;
import org.eclipse.jdt.annotation.Nullable;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.skript.SkriptEnchantment;

import java.io.StreamCorruptedException;

public class BaseSerializer extends Serializer<SkriptEnchantment> {
    @Override
    public Fields serialize(final SkriptEnchantment ench) {
        Fields fields = new Fields();
        fields.putObject("enchant", ench);
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
        return (SkriptEnchantment) fields.getObject("enchant");
    }

    @Override
    @Nullable
    public SkriptEnchantment deserialize(String s) {
        for (SkriptEnchantment enchantment : ((EnchantmentTokens) Bukkit.getPluginManager().getPlugin("EnchantmentTokens")).getEnchantmentHandler().getSkriptEnchantments()) {
            if (enchantment.getName().equals(s))
                return enchantment;
        }
        return null;
    }

    @Override
    public boolean mustSyncDeserialization() {
        return false;
    }
}
