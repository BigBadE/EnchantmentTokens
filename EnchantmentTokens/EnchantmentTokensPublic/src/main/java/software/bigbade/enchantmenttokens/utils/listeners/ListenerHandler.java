package software.bigbade.enchantmenttokens.utils.listeners;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.api.EnchantmentAddon;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.ListenerType;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface ListenerHandler {
    void registerListeners();

    void loadAddons(Collection<EnchantmentAddon> addons);

    void onEnchant(ItemStack item, EnchantmentBase base, Player player);

    void loadEnchantments(Map<String, Set<Class<EnchantmentBase>>> enchants);

    ListenerManager getListenerManager(ListenerType type);
}
