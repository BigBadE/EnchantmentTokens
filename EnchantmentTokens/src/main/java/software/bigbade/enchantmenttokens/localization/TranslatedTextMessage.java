package software.bigbade.enchantmenttokens.localization;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class TranslatedTextMessage implements ITranslatedText {
    private static Map<String, ResourceBundle> bundles = new HashMap<>();

    private final String resource;

    public TranslatedTextMessage(String key) {
        resource = bundles.get("messages").getString(key);
    }

    public TranslatedTextMessage(String namespace, String key) {
        resource = bundles.get(namespace).getString(key);
    }

    public static void updateBundles(Map<String, ResourceBundle> bundles) {
        TranslatedTextMessage.bundles = bundles;
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
