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
        super(player, "gemsNew");
        this.gems = gems;
        this.locale = locale;
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        setAmount(dataContainer.getOrDefault(gems, PersistentDataType.LONG, 0L));
        Locale defaultLocale = Locale.forLanguageTag(player.getLocale());
        if (defaultLocale.getLanguage().isEmpty()) {
            //Some resource packs can mess this up
            defaultLocale = Locale.getDefault();
        }

        setLocale(Locale.forLanguageTag(dataContainer.getOrDefault(locale, PersistentDataType.STRING, defaultLocale.toLanguageTag())));
    }

    @Override
    public void savePlayer(Player player) {
        player.getPersistentDataContainer().set(gems, PersistentDataType.LONG, getGems());
        player.getPersistentDataContainer().set(locale, PersistentDataType.STRING, getLocale().toLanguageTag());
    }
}