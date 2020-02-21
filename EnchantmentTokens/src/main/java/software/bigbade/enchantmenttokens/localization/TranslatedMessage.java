package software.bigbade.enchantmenttokens.localization;

import software.bigbade.enchantmenttokens.EnchantmentTokens;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class TranslatedMessage {
    private static Map<String, ResourceBundle> bundles = new HashMap<>();

    public static String translateNamespace(String key, String namespace, String... arguments) {
        for (Map.Entry<String, ResourceBundle> entry : bundles.entrySet()) {
            if (entry.getKey().equals(namespace)) {
                String message = entry.getValue().getString(key);
                for(String argument : arguments)
                    message = message.replaceFirst("%s", argument.replace("$", "\\$"));
                message = ChatColor.translateAlternateColorCodes('&', message);
                return message;
            }
        }
        return "KEY NOT FOUND";
    }

    public static String translate(String key, String... arguemnts) {
        return translateNamespace(key, EnchantmentTokens.NAME, arguemnts);
    }

    public static void updateBundles(Map<String, ResourceBundle> bundles) {
        TranslatedMessage.bundles = bundles;
    }
}
