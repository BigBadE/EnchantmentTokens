package software.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.utils.enchants.FakePlugin;

public class LatestCurrencyFactory extends CurrencyFactory {
    private NamespacedKey key;

    public LatestCurrencyFactory() {
        super("gems");
        this.key = new NamespacedKey(FakePlugin.ENCHANTMENTPLUGIN, "gems");
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
