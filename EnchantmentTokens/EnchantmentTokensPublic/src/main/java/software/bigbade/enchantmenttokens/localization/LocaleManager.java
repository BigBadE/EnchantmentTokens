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

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.configuration.ConfigurationType;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class LocaleManager {
    private static Map<String, ResourceBundle> bundles = new HashMap<>();

    private static Locale locale;

    private LocaleManager() { }

    public static void updateLocale(ConfigurationSection section, Collection<Plugin> addons) {
        locale = getLocale(section);

        try {
            Map<String, ResourceBundle> resources = new HashMap<>();
            resources.put(EnchantmentTokens.NAME, new PropertyResourceBundle(getStream(EnchantmentTokens.NAME, locale)));

            for (Plugin addon : addons) {
                resources.put(addon.getName(), new PropertyResourceBundle(getStream(addon.getName(), locale)));
            }

            LocaleManager.updateBundles(resources);
        } catch (IOException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not load Localization files.", e);
        }
    }

    private static Locale getLocale(ConfigurationSection section) {
        locale = Locale.US;
        String language = new ConfigurationType<>("US").getValue("country-language", section);

        for (Locale foundLocale : Locale.getAvailableLocales()) {
            if (foundLocale.getCountry() != null && foundLocale.getDisplayCountry().equals(language)) {
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

    public static void updateBundles(Map<String, ResourceBundle> bundles) {
        LocaleManager.bundles = bundles;
    }

    public static Locale getLocale() {
        return locale;
    }

    @Nullable
    public static ResourceBundle getBundle(String namespace) {
        return bundles.getOrDefault(namespace, null);
    }
}
