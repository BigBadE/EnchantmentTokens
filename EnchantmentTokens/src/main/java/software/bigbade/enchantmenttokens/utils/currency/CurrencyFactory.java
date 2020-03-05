package software.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.entity.Player;

public abstract class CurrencyFactory {
    private String name;

    public CurrencyFactory(String name) {
        this.name = name;
    }

    public abstract CurrencyHandler newInstance(Player player);

    public String name() {
        return name;
    }

    public void shutdown() {

    }

    public abstract boolean loaded();
}
