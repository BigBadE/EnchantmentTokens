package software.bigbade.enchantmenttokens.localization;

import org.bukkit.ChatColor;
import software.bigbade.enchantmenttokens.EnchantmentTokens;

import java.util.ResourceBundle;

public class TranslatedString implements ITranslatedMessage {
    private String message;

    //Private constructor to hide implicit public one.
    public TranslatedString(String namespace, String key) {
        ResourceBundle bundle = LocaleManager.getBundle(namespace);
        if (bundle != null) {
            message = ChatColor.translateAlternateColorCodes('&', bundle.getString(key));
        } else {
            message = "NO BUNDLE FOUND";
        }
    }

    public TranslatedString(String key) {
        this(EnchantmentTokens.NAME, key);
    }

    @Override
    public String translate(String... args) {
        String text = message;
        for (String argument : args) {
            text = text.replaceFirst("%s", argument.replace("$", "\\$"));
        }
        return text;
    }
}
