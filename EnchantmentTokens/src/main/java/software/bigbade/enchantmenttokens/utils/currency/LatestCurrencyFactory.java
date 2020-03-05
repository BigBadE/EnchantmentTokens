package software.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

public class LatestCurrencyFactory extends CurrencyFactory {
    private NamespacedKey key;

    public LatestCurrencyFactory(NamespacedKey key) {
        super("gems");
        this.key = key;
    }

    @Override
    public CurrencyHandler newInstance(Player player) {
        return new LatestCurrencyHandler(player, key);
    }

    @Override
    public boolean loaded() {
        return true;
    }
}
