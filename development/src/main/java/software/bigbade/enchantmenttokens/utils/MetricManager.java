/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.utils;

import org.bstats.bukkit.Metrics;
import software.bigbade.enchantmenttokens.EnchantmentTokens;

public class MetricManager {
    public MetricManager(EnchantmentTokens main) {
        Metrics metrics = new Metrics(main, 6283);
        metrics.addCustomChart(new Metrics.SimplePie("currency_methods", () -> main.getCurrencyHandler().name()));
        metrics.addCustomChart(new Metrics.SimplePie("language", () -> main.getConfig().getString("country-language")));
    }
}
