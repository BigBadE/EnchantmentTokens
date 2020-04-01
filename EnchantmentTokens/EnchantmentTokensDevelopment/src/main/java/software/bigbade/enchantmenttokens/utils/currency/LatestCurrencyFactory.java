/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

public class LatestCurrencyFactory extends EnchantCurrencyFactory {
    private final NamespacedKey gems;
    private final NamespacedKey locale;

    public LatestCurrencyFactory(NamespacedKey gems, NamespacedKey locale) {
        super("gems");
        this.gems = gems;
        this.locale = locale;
    }

    @Override
    public EnchantCurrencyHandler newInstance(Player player) {
        return new LatestCurrencyHandler(player, gems, locale);
    }

    @Override
    public boolean loaded() {
        return true;
    }
}
