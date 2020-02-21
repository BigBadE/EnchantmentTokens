package software.bigbade.enchantmenttokens.api;

import software.bigbade.enchantmenttokens.gui.EnchantmentGUI;
import software.bigbade.enchantmenttokens.utils.currency.CurrencyHandler;
import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.utils.currency.VaultCurrencyHandler;

import java.io.Serializable;

public class EnchantmentPlayer implements Serializable {
    private Player player;

    private EnchantmentGUI currentGUI;

    private CurrencyHandler handler;

    private EnchantmentPlayer(Player player) {
        this.player = player;
    }

    public static EnchantmentPlayer loadPlayer(Player player) {
        return new EnchantmentPlayer(player);
    }

    public void save() {
        handler.savePlayer(player);
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

    public void setCurrentGUI(EnchantmentGUI currentGUI) {
        this.currentGUI = currentGUI;
    }

    public void setHandler(CurrencyHandler handler) {
        this.handler = handler;
    }
}
