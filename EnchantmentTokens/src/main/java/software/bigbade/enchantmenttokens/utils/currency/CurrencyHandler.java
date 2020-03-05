package software.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.entity.Player;

public abstract class CurrencyHandler {
    private long gems = -1;
    private String name;

    public CurrencyHandler(String name) {
        this.name = name;
    }

    public long getAmount() { return gems; }

    public void setAmount(long amount) { gems = amount; }

    public void addAmount(long amount) { gems += amount; }

    public void savePlayer(Player player, boolean async) {

    }

    public String name() {
        return name;
    }
}
