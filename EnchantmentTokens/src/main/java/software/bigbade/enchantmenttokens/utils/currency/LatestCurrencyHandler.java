package software.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class LatestCurrencyHandler implements CurrencyHandler {
    private long gems;
    private NamespacedKey key;

    public LatestCurrencyHandler(Player player, NamespacedKey key) {
        this.key = key;
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        gems = dataContainer.getOrDefault(key, PersistentDataType.LONG, 0L);
    }

    @Override
    public long getAmount() {
        return gems;
    }

    @Override
    public void setAmount(long amount) {
        gems = amount;
    }

    @Override
    public void addAmount(long amount) {
        gems += amount;
    }

    @Override
    public void savePlayer(Player player) {
        player.getPersistentDataContainer().set(key, PersistentDataType.LONG, gems);
    }

    @Override
    public String name() {
        return "gemsNew";
    }
}