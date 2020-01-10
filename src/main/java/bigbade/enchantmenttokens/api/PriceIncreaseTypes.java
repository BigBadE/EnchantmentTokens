package bigbade.enchantmenttokens.api;

import org.bukkit.configuration.ConfigurationSection;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public enum PriceIncreaseTypes {
    TEST((level, section) -> {
        return level + 1;
    });

    private BiFunction<Integer, ConfigurationSection, Integer> function;
    PriceIncreaseTypes(BiFunction<Integer, ConfigurationSection, Integer> function) {
        this.function = function;
    }

    public long getPrice(int level, ConfigurationSection section) {
        return function.apply(level, section);
    }
}
