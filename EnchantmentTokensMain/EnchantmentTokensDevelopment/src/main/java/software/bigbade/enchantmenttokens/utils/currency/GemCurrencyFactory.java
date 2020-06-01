/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.loader.FileLoader;
import software.bigbade.enchantmenttokens.utils.SchedulerHandler;

public class GemCurrencyFactory extends EnchantCurrencyFactory {
    private final SchedulerHandler scheduler;
    private final FileLoader loader;

    public GemCurrencyFactory(SchedulerHandler scheduler, String dataPath) {
        super("gems");
        this.scheduler = scheduler;
        loader = new FileLoader(dataPath);
    }

    public EnchantCurrencyHandler newInstance(Player player) {
        return new GemCurrencyHandler(player, loader, scheduler);
    }

    @Override
    public boolean loaded() {
        return true;
    }
}