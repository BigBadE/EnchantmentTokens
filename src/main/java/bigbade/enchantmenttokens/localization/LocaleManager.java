package bigbade.enchantmenttokens.localization;

import bigbade.enchantmenttokens.EnchantmentTokens;
import bigbade.enchantmenttokens.api.EnchantmentAddon;
import bigbade.enchantmenttokens.utils.EnchantLogger;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;

public class LocaleManager {
    private LocaleManager() { }

    public static void updateLocale(Locale locale, Collection<EnchantmentAddon> addons) {
        try {
            Map<String, ResourceBundle> resources = new HashMap<>();
            resources.put(EnchantmentTokens.NAME, new PropertyResourceBundle(getStream("messages", locale)));

            for(EnchantmentAddon addon : addons) {
                resources.put(addon.getName(), new PropertyResourceBundle(getStream(addon.getName(), locale)));
            }

            TranslatedMessage.updateBundles(resources);
        } catch (IOException e) {
            EnchantLogger.LOGGER.log(Level.SEVERE, "Could not load Localization files.", e);
        }
    }

    private static InputStream getStream(String name, Locale locale) {
        InputStream languageStream = EnchantmentTokens.class.getResourceAsStream("/Localization/" + name + "-" + locale.getLanguage() + "_" + locale.getCountry() + ".properties");
        if(languageStream == null)
            languageStream = EnchantmentTokens.class.getResourceAsStream("Localization/messages-en_US.properties");
        return languageStream;
    }
}
