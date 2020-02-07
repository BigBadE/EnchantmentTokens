package software.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.api.ExternalCurrencyData;

public interface CurrencyFactory {
    CurrencyHandler newInstance(Player player);

    String name();

    void setData(ExternalCurrencyData data);

    ExternalCurrencyData getData();

    void shutdown();
}
