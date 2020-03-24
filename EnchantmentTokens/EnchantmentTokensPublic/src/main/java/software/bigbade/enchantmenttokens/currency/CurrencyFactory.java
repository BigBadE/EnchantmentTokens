package software.bigbade.enchantmenttokens.currency;

import org.bukkit.entity.Player;

public interface CurrencyFactory {
    CurrencyHandler newInstance(Player player);

    String name();

    void shutdown();

    boolean loaded();
}
