package software.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.api.ExternalCurrencyData;

public class LatestCurrencyFactory implements CurrencyFactory {
    private NamespacedKey key;
    private ExternalCurrencyData data;

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

    @Override
    public void setData(ExternalCurrencyData data) {
        this.data = data;
    }

    @Override
    public ExternalCurrencyData getData() {
        return data;
    }

    @Override
    public void shutdown() {
        //Not used since Spigot handles this for us
    }
}
