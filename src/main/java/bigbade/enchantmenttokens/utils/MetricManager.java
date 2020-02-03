package bigbade.enchantmenttokens.utils;

import bigbade.enchantmenttokens.EnchantmentTokens;
import org.bstats.bukkit.Metrics;

public class MetricManager {
    public MetricManager(EnchantmentTokens main) {
        Metrics metrics = new Metrics(main, 6283);
        metrics.addCustomChart(new Metrics.SimplePie("currency_methods", () -> main.getCurrencyHandler().name()));
        metrics.addCustomChart(new Metrics.SimplePie("language", () -> main.getConfig().getString("country-language")));
    }
}
