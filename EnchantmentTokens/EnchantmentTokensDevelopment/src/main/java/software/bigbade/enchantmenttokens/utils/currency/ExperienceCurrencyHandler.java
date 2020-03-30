package software.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.currency.CurrencyHandler;

public class ExperienceCurrencyHandler implements CurrencyHandler {
    private Player player;

    public ExperienceCurrencyHandler(Player player) {
        this.player = player;
    }
    @Override
    public long getAmount() {
        return player.getLevel();
    }

    @Override
    public void setAmount(long amount) {
        player.setLevel((int) amount);
    }

    @Override
    public void addAmount(long amount) {
        player.giveExpLevels((int) amount);
    }

    @Override
    public void savePlayer(Player player, boolean async) {
        //Not used, because Bukkit handles it
    }

    @Override
    public String name() {
        return "experience";
    }
}
