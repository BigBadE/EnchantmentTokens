package software.bigbade.enchantmenttokens.localization;

import software.bigbade.enchantmenttokens.EnchantmentTokens;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class TranslatedTextMessage implements ITranslatedText {
    private static final Map<String, ResourceBundle> bundles = new HashMap<>();

    private final String resource;

    public TranslatedTextMessage(String key) {
        resource = bundles.get(EnchantmentTokens.NAME).getString(key);
    }

    public TranslatedTextMessage(String namespace, String key) {
        resource = bundles.get(namespace).getString(key);
    }

    public static void updateBundles(Map<String, ResourceBundle> bundles) {
        TranslatedTextMessage.bundles.putAll(bundles);
    }

    @Override
    public String getText() {
        return resource;
    }

    @Override
    public String getText(String... args) {
        String replaced = resource;
        for(String arg : args)
            replaced = replaced.replaceFirst("%s", arg);
        return replaced;
    }
}
