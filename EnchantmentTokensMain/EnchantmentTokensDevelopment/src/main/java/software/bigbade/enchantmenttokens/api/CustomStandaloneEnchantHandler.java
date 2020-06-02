package software.bigbade.enchantmenttokens.api;

import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerHandler;

@RequiredArgsConstructor
public class CustomStandaloneEnchantHandler extends StandaloneEnchantHandler {
    private final ListenerHandler listenerHandler;

    @Override
    public void createEnchantment(Plugin plugin, Class<? extends EnchantmentBase> clazz) {
        listenerHandler.loadEnchantment(plugin, clazz);
    }
}
