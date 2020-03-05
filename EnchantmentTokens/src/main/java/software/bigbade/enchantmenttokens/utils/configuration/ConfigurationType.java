package software.bigbade.enchantmenttokens.utils.configuration;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

/**
 * Used for safe casting of unknown configuration types
 */
public class ConfigurationType<T> {
    private T defaultValue;

    public ConfigurationType(@NotNull T defaultValue) {
        this.defaultValue = defaultValue;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public T getValue(@NotNull String value, @NotNull ConfigurationSection section) {
        try {
            T foundValue = (T) section.get(value);
            if(foundValue == null) {
                section.set(value, defaultValue);
                return defaultValue;
            }
            return foundValue;
        } catch (ClassCastException e) {
            section.set(value, defaultValue);
            return defaultValue;
        }
    }
}
