package software.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.entity.Player;

public interface CurrencyHandler {
    long getAmount();

    void setAmount(long amount);

    void addAmount(long amount);

    void savePlayer(Player player);

    String name();
}
