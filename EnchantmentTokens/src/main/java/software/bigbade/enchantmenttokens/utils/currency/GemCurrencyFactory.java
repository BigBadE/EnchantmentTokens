package software.bigbade.enchantmenttokens.utils.currency;

import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.ExternalCurrencyData;
import software.bigbade.enchantmenttokens.loader.FileLoader;
import org.bukkit.entity.Player;

public class GemCurrencyFactory implements CurrencyFactory {
    private EnchantmentTokens main;
    private FileLoader loader;
    private ExternalCurrencyData data;

    public GemCurrencyFactory(EnchantmentTokens main) {
        this.main = main;
        loader = new FileLoader(main.getDataFolder().getAbsolutePath());
    }

    public CurrencyHandler newInstance(Player player) {
        return new GemCurrencyHandler(player, loader, main.getScheduler());
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
