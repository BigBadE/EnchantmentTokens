package software.bigbade.enchantmenttokens.skript;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import software.bigbade.enchantmenttokens.api.CustomEnchantment;

public class SkriptEnchantment extends CustomEnchantment {
    public SkriptEnchantment(NamespacedKey key, String name, Material icon) {
        super(key, icon, name);
    }
}
