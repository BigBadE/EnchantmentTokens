package software.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.currency.CurrencyFactory;
import software.bigbade.enchantmenttokens.currency.CurrencyHandler;

public class ExperienceCurrencyFactory implements CurrencyFactory {
    @Override
    public CurrencyHandler newInstance(Player player) {
        return new ExperienceCurrencyHandler(player);
    }

    @Override
    public String name() {
        return "experience";
    }

    @Override
    public void shutdown() {
        //Not used since Bukkit handles it
    }

    @Override
    public boolean loaded() {
        return true;
    }
}
