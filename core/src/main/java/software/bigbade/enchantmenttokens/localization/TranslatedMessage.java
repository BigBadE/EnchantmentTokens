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
