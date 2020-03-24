package software.bigbade.enchantmenttokens.currency;

import org.bukkit.entity.Player;

public interface CurrencyHandler {
    long getAmount();

    void setAmount(long amount);

    void addAmount(long amount);

    void savePlayer(Player player, boolean async);

    String name();
}
