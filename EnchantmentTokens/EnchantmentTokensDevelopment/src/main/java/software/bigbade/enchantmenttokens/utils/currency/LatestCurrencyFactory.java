package software.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

public class LatestCurrencyFactory extends EnchantCurrencyFactory {
    private NamespacedKey key;

    public LatestCurrencyFactory(NamespacedKey key) {
        super("gems");
        this.key = key;
    }

    @Override
    public EnchantCurrencyHandler newInstance(Player player) {
        return new LatestCurrencyHandler(player, key);
    }

    @Override
    public boolean loaded() {
        return true;
    }
}
