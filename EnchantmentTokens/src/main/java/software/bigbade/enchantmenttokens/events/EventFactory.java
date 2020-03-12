package software.bigbade.enchantmenttokens.events;

import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.api.ListenerType;

public class EventFactory {
    //Private constructor to hide implicit public one
    private EventFactory() { }

    public static EnchantmentEvent createEvent(ListenerType listener, ItemStack item) {
        return new CustomEnchantEvent(listener, item);
    }
}
