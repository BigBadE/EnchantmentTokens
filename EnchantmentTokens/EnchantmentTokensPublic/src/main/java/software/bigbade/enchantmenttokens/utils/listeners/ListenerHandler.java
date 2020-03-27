package software.bigbade.enchantmenttokens.utils.listeners;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import software.bigbade.enchantmenttokens.api.EnchantmentAddon;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.ListenerType;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface ListenerHandler {
    void registerListeners();

    void loadAddons(Collection<Plugin> addons);

    void onEnchant(ItemStack item, EnchantmentBase base, Player player);

    void loadEnchantments(Map<EnchantmentAddon, Set<Class<EnchantmentBase>>> enchants);

    ListenerManager getListenerManager(ListenerType type);
}
