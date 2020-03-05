package software.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.loader.FileLoader;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;

public class GemCurrencyFactory extends CurrencyFactory {
    private SchedulerHandler scheduler;
    private FileLoader loader;

    public GemCurrencyFactory(EnchantmentTokens main) {
        super("gems");
        this.scheduler = main.getScheduler();
        loader = new FileLoader(main.getDataFolder().getAbsolutePath());
    }

    public CurrencyHandler newInstance(Player player) {
        return new GemCurrencyHandler(player, loader, scheduler);
    }

    @Override
    public boolean loaded() {
        return true;
    }
}
