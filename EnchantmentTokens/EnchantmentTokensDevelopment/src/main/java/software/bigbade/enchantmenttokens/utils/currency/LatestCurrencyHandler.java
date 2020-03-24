package software.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class LatestCurrencyHandler extends EnchantCurrencyHandler {
    private NamespacedKey key;

    public LatestCurrencyHandler(Player player, NamespacedKey key) {
        super("gemsNew");
        this.key = key;
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        setAmount(dataContainer.getOrDefault(key, PersistentDataType.LONG, 0L));
    }

    @Override
    public void savePlayer(Player player, boolean async) {
        player.getPersistentDataContainer().set(key, PersistentDataType.LONG, getAmount());
    }
}