/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.utils.currency;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Locale;

public class LatestCurrencyHandler extends EnchantCurrencyHandler {
    private final NamespacedKey gems;
    private final NamespacedKey locale;

    public LatestCurrencyHandler(Player player, NamespacedKey gems, NamespacedKey locale) {
        super("gemsNew");
        this.gems = gems;
        this.locale = locale;
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        setAmount(dataContainer.getOrDefault(gems, PersistentDataType.LONG, 0L));
        setLocale(Locale.forLanguageTag(dataContainer.getOrDefault(locale, PersistentDataType.STRING, Locale.getDefault().toLanguageTag())));
    }

    @Override
    public void savePlayer(Player player, boolean async) {
        player.getPersistentDataContainer().set(gems, PersistentDataType.LONG, getAmount());
        player.getPersistentDataContainer().set(locale, PersistentDataType.STRING, getLocale().toLanguageTag());
    }
}