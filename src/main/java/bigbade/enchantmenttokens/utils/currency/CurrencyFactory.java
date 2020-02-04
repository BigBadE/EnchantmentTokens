package bigbade.enchantmenttokens.utils.currency;

import org.bukkit.entity.Player;

public interface CurrencyFactory {

    CurrencyHandler newInstance(Player player);
}
