package bigbade.enchantmenttokens.utils.currency;

import bigbade.enchantmenttokens.EnchantmentTokens;
import org.bukkit.entity.Player;

public class LatestCurrencyFactory implements CurrencyFactory {
    private EnchantmentTokens main;
    public LatestCurrencyFactory(EnchantmentTokens main) {
        this.main = main;
    }

    @Override
    public CurrencyHandler newInstance(Player player) {
        return new LatestCurrencyHandler();
    }
}
