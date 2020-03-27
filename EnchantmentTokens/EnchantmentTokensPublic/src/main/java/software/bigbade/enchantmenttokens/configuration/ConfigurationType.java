package software.bigbade.enchantmenttokens.configuration;

import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;

/**
 * Used for safe casting of unknown configuration types
 */
public class ConfigurationType<T> {
    private T defaultValue;

    public ConfigurationType(@Nonnull T defaultValue) {
        this.defaultValue = defaultValue;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public T getValue(@Nonnull String value, @Nonnull ConfigurationSection section) {
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
