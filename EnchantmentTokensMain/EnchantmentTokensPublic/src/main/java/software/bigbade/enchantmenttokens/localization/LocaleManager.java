/*
 * Addons for the Custom Enchantment API in Minecraft
 * Copyright (C) 2020 BigBadE
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package software.bigbade.enchantmenttokens.localization;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.EnchantmentAddon;
import software.bigbade.enchantmenttokens.configuration.ConfigurationType;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class LocaleManager {
    private static final Map<Locale, List<AddonResourceBundle>> bundles = new HashMap<>();

    @Getter
    private static final Locale[] supportedLocales = new Locale[]{Locale.US, Locale.UK};

    private LocaleManager() {
    }

    public static void updateLocale(ConfigurationSection section, Collection<EnchantmentAddon> addons) {
        getLocale(section);

        try {
            for (Locale locale : supportedLocales) {
                InputStream stream = getStream(EnchantmentTokens.NAME, locale);
                if (stream != null)
                    createOrAddLocale(locale, new AddonResourceBundle(EnchantmentTokens.NAME, new PropertyResourceBundle(stream)));
            }

            for (EnchantmentAddon addon : addons) {
                for (Locale locale : addon.supportedLocales()) {
                    InputStream stream = getStream(addon.getName(), locale);
                    if (stream != null)
                        createOrAddLocale(locale, new AddonResourceBundle(addon.getName(), new PropertyResourceBundle(stream)));
                }
            }
        } catch (IOException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not load Localization files.", e);
        }
    }

    private static void createOrAddLocale(Locale locale, AddonResourceBundle bundle) {
        List<AddonResourceBundle> found = bundles.get(locale);
        if (found == null) {
            found = new ArrayList<>();
            found.add(bundle);
            bundles.put(locale, found);
        } else {
            found.add(bundle);
        }
    }

    private static void getLocale(ConfigurationSection section) {
        Locale.setDefault(Locale.US);
        String language = new ConfigurationType<>("US").getValue("country-language", section);

        for (Locale foundLocale : Locale.getAvailableLocales()) {
            if (foundLocale.getCountry() != null && foundLocale.getDisplayCountry().equals(language)) {
                Locale.setDefault(foundLocale);
                break;
            }
        }
    }

    private static InputStream getStream(String name, Locale locale) {
        InputStream languageStream = EnchantmentTokens.class.getResourceAsStream("/localization/" + name + "-" + locale.getLanguage() + "_" + locale.getCountry() + ".properties");
        if (languageStream == null)
            languageStream = EnchantmentTokens.class.getResourceAsStream("localization/" + EnchantmentTokens.NAME + "-en_US.properties");
        return languageStream;
    }

    @Nullable
    public static ResourceBundle getBundle(Locale locale, String namespace) {
        for (AddonResourceBundle bundle : bundles.get(locale)) {
            if (bundle.getAddon().equals(namespace))
                return bundle.getBundle();
        }
        return (locale == Locale.US) ? getBundle(Locale.US, EnchantmentTokens.NAME) : getBundle(Locale.US, namespace);
    }
}
