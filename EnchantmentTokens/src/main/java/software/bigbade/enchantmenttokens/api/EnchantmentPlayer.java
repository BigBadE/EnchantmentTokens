package software.bigbade.enchantmenttokens.api;

import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.gui.EnchantmentGUI;
import software.bigbade.enchantmenttokens.utils.currency.CurrencyHandler;
import software.bigbade.enchantmenttokens.utils.currency.VaultCurrencyHandler;

public class EnchantmentPlayer {
    private Player player;

    private EnchantmentGUI currentGUI;

    private CurrencyHandler handler;

    private long doubler = 1;

    private EnchantmentPlayer(Player player) {
        this.player = player;
    }

    public static EnchantmentPlayer loadPlayer(Player player) {
        return new EnchantmentPlayer(player);
    }

    public void save(boolean async) {
        handler.savePlayer(player, async);
    }

    public Player getPlayer() {
        return player;
    }

    public long getGems() {
        return handler.getAmount();
    }

    public void addGems(long amount) {
        handler.addAmount(amount);
    }

    public boolean usingGems() {
        return !(handler instanceof VaultCurrencyHandler);
    }

    public EnchantmentGUI getCurrentGUI() {
        return currentGUI;
    }

    public long getDoubler() {
        return doubler;
    }

    public void removeDoubler(long removing) {
        doubler -= removing;
    }

    public void resetDoubler() {
        doubler = 1;
    }

    /**
     * Adds a doubler to the user
     * @param adding the amount to add, adding 1 gives a 2x bonus, 3 goes to a 4x bonus, .5 goes to a 1.5x bonus, etc...
     */
    public void addDoubler(long adding) {
        doubler += adding;
    }

    public void setCurrentGUI(EnchantmentGUI currentGUI) {
        this.currentGUI = currentGUI;
    }

    public void setHandler(CurrencyHandler handler) {
        this.handler = handler;
    }
}
