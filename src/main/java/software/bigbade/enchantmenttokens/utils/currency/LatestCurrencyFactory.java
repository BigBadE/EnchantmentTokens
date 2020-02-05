package software.bigbade.enchantmenttokens.utils.currency;

import software.bigbade.enchantmenttokens.EnchantmentTokens;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

public class LatestCurrencyFactory implements CurrencyFactory {
    private NamespacedKey key;
    public LatestCurrencyFactory(EnchantmentTokens main) {
        key = new NamespacedKey(main, "gems");
    }

    @Override
    public CurrencyHandler newInstance(Player player) {
        return new LatestCurrencyHandler(player, key);
    }

    public String name() {
        return "gemsNew";
    }
}
