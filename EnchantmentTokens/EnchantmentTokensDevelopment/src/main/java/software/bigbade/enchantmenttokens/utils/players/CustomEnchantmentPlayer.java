package software.bigbade.enchantmenttokens.utils.players;

import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.currency.CurrencyHandler;
import software.bigbade.enchantmenttokens.gui.EnchantmentGUI;

public class CustomEnchantmentPlayer implements EnchantmentPlayer {
    private Player player;

    private EnchantmentGUI currentGUI;

    private CurrencyHandler handler;

    private long doubler = 1;

    public CustomEnchantmentPlayer(Player player) {
        this.player = player;
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
