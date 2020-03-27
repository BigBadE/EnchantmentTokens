package software.bigbade.enchantmenttokens.api;

import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;

public class EventFactory {
    //Private constructor to hide implicit one
    private EventFactory() {}

    public static EnchantmentEvent createEvent(ListenerType type, ItemStack item) {
        return new CustomEnchantmentEvent(type, item);
    }
}
