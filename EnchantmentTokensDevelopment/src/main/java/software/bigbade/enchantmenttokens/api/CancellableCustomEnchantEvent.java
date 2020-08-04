package software.bigbade.enchantmenttokens.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.events.CancellableEnchantmentEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CancellableCustomEnchantEvent<T extends Event & Cancellable> extends CustomEnchantmentEvent<T> implements CancellableEnchantmentEvent<T> {
    public CancellableCustomEnchantEvent(@Nonnull T event, @Nullable ItemStack item, @Nonnull Player user) {
        super(event, item, user);
    }

    @Override
    public boolean isCancelled() {
        return getEvent().isCancelled();
    }

    public void setCancelled(boolean cancelled) {
        getEvent().setCancelled(cancelled);
    }
}
