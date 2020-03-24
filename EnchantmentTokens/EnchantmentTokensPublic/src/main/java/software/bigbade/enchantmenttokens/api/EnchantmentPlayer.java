package software.bigbade.enchantmenttokens.api;

import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.currency.CurrencyHandler;
import software.bigbade.enchantmenttokens.gui.EnchantmentGUI;

public interface EnchantmentPlayer {
    void save(boolean async);

    Player getPlayer();

    long getGems();

    void addGems(long amount);

    EnchantmentGUI getCurrentGUI();

    long getDoubler();

    void removeDoubler(long removing);

    void resetDoubler();

    /**
     * Adds a doubler to the user
     * @param adding the amount to add, adding 1 gives a 2x bonus, 3 goes to a 4x bonus, .5 goes to a 1.5x bonus, etc...
     */
    void addDoubler(long adding);

    void setCurrentGUI(EnchantmentGUI currentGUI);

    void setHandler(CurrencyHandler handler);
}
