package software.bigbade.enchantmenttokens.api;

import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentLoader;

@RequiredArgsConstructor
public class CustomStandaloneEnchantHandler extends StandaloneEnchantHandler {
    private final EnchantmentLoader enchantmentLoader;

    @Override
    public void createEnchantment(Plugin plugin, Class<? extends EnchantmentBase> clazz) {
        enchantmentLoader.loadEnchantment(plugin, clazz);
    }
}
