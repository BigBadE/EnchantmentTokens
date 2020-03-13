package software.bigbade.enchantmenttokens.localization;

import org.bukkit.configuration.ConfigurationSection;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.EnchantmentAddon;
import software.bigbade.enchantmenttokens.utils.EnchantLogger;
import software.bigbade.enchantmenttokens.utils.configuration.ConfigurationType;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;

public class LocaleManager {
    public static Locale locale;
    private LocaleManager() { }

    public static void updateLocale(ConfigurationSection section, Collection<EnchantmentAddon> addons) {
        locale = getLocale(section);

        try {
            Map<String, ResourceBundle> resources = new HashMap<>();
            resources.put(EnchantmentTokens.NAME, new PropertyResourceBundle(getStream("messages", locale)));

            for (EnchantmentAddon addon : addons) {
                resources.put(addon.getName(), new PropertyResourceBundle(getStream(addon.getName(), locale)));
            }

            TranslatedTextMessage.updateBundles(resources);
        } catch (IOException e) {
            EnchantLogger.log(Level.SEVERE, "Could not load Localization files.", e);
        }
    }

    private static Locale getLocale(ConfigurationSection section) {
        Locale locale = Locale.US;
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
}
