package software.bigbade.enchantmenttokens.utils.configuration;

import org.bukkit.configuration.ConfigurationSection;

/**
 * Used for safe casting of unknown configuration types
 */
public class ConfigurationType<T> {
    private Object defaultValue;

    public ConfigurationType(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public T getValue(String value, ConfigurationSection section) {
        try {
            return (T) section.get(value);
        } catch (ClassCastException e) {
            return (T) defaultValue;
        }
    }
}
