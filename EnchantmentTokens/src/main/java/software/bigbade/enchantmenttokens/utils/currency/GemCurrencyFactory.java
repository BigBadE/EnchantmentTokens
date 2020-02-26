package software.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.loader.FileLoader;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;

public class GemCurrencyFactory implements CurrencyFactory {
    private SchedulerHandler scheduler;
    private FileLoader loader;

    public GemCurrencyFactory(EnchantmentTokens main) {
        this.scheduler = main.getScheduler();
        loader = new FileLoader(main.getDataFolder().getAbsolutePath());
    }

    public CurrencyHandler newInstance(Player player) {
        return new GemCurrencyHandler(player, loader, scheduler);
    }

    @Override
    public String name() {
        return "gemsOld";
    }

    @Override
    public void shutdown() {
        //Not used
    }

    @Override
    public boolean loaded() {
        return true;
    }
}
