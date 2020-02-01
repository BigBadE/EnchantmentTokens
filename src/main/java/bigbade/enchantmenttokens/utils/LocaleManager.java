package bigbade.enchantmenttokens.utils;

import bigbade.enchantmenttokens.EnchantmentTokens;
import bigbade.enchantmenttokens.api.EnchantmentAddon;
import bigbade.enchantmenttokens.localization.TranslatedMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class LocaleManager {
    public static void updateLocale(Locale locale, Collection<EnchantmentAddon> addons) {
        try {
            Map<String, ResourceBundle> resources = new HashMap<>();
            resources.put(EnchantmentTokens.NAME, new PropertyResourceBundle(getStream("messages", locale)));

            for(EnchantmentAddon addon : addons) {
                resources.put(addon.getName(), new PropertyResourceBundle(getStream(addon.getName(), locale)));
            }

            TranslatedMessage.updateBundles(resources);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static InputStream getStream(String name, Locale locale) {
        InputStream languageStream = EnchantmentTokens.class.getResourceAsStream(name + "-" + locale.toLanguageTag() + "_" + locale.getCountry() + ".properties");
        if(languageStream == null)
            languageStream = EnchantmentTokens.class.getResourceAsStream("messages-en_US.properties");
        return languageStream;
    }
}
