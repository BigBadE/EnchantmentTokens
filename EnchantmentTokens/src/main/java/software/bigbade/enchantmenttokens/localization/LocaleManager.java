package software.bigbade.enchantmenttokens.localization;

/*
EnchantmentTokens
Copyright (C) 2019-2020 Big_Bad_E
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

import org.bukkit.configuration.ConfigurationSection;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.EnchantmentAddon;
import software.bigbade.enchantmenttokens.utils.configuration.ConfigurationManager;
import software.bigbade.enchantmenttokens.utils.EnchantLogger;
import software.bigbade.enchantmenttokens.utils.configuration.ConfigurationType;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;

public class LocaleManager {
    private LocaleManager() {
    }

    public static void updateLocale(ConfigurationSection section, Collection<EnchantmentAddon> addons) {
        Locale locale = getLocale(section);

        try {
            Map<String, ResourceBundle> resources = new HashMap<>();
            resources.put(EnchantmentTokens.NAME, new PropertyResourceBundle(getStream("messages", locale)));

            for (EnchantmentAddon addon : addons) {
                resources.put(addon.getName(), new PropertyResourceBundle(getStream(addon.getName(), locale)));
            }

            TranslatedMessage.updateBundles(resources);
        } catch (IOException e) {
            EnchantLogger.log(Level.SEVERE, "Could not load Localization files.", e);
        }
    }

    private static Locale getLocale(ConfigurationSection section) {
        Locale locale = Locale.US;
        String language = new ConfigurationType<String>("US").getValue("country-language", section);

        for (Locale foundLocale : Locale.getAvailableLocales()) {
            if (foundLocale.getCountry() != null)
                continue;
            if (foundLocale.getDisplayCountry().equals(language)) {
                locale = foundLocale;
                break;
            }
        }
        return locale;
    }

    private static InputStream getStream(String name, Locale locale) {
        InputStream languageStream = EnchantmentTokens.class.getResourceAsStream("/localization/" + name + "-" + locale.getLanguage() + "_" + locale.getCountry() + ".properties");
        if (languageStream == null)
            languageStream = EnchantmentTokens.class.getResourceAsStream("localization/messages-en_US.properties");
        return languageStream;
    }
}
