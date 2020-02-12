package software.bigbade.enchantmenttokens.skript.type;

import ch.njol.skript.classes.Serializer;
import ch.njol.yggdrasil.Fields;
import org.bukkit.Material;
import org.eclipse.jdt.annotation.Nullable;
import software.bigbade.enchantmenttokens.skript.SkriptEnchantment;

public class BaseSerializer extends Serializer<SkriptEnchantment> {
    @Override
    public Fields serialize(final SkriptEnchantment ench) {
        return new Fields();
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
    protected SkriptEnchantment deserialize(final Fields fields) {
        return new SkriptEnchantment("do-not-save-enchantments", Material.BEDROCK);
    }

    @Override
    @Nullable
    public SkriptEnchantment deserialize(String s) {
        return new SkriptEnchantment("do-not-save-enchantments", Material.BEDROCK);
    }

    @Override
    public boolean mustSyncDeserialization() {
        return false;
    }
}
