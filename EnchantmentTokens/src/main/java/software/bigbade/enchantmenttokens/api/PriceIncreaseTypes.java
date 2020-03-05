package software.bigbade.enchantmenttokens.api;

import org.bukkit.configuration.ConfigurationSection;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public enum PriceIncreaseTypes {
    CUSTOM((level, section) ->
            section.getInt(level + ""), enchant -> {
        for (int i = enchant.minLevel; i < enchant.maxLevel + 1; i++) {
            if (enchant.price.get(i + "") == null) {
                enchant.price.set(i + "", i * 10);
            }
        }
        for (String key : enchant.price.getKeys(true)) {
            try {
                if (!key.equals("type") && (Integer.parseInt(key) < enchant.minLevel || Integer.parseInt(key) > enchant.maxLevel + 1)) {
                    enchant.price.set(key, null);
                }
            } catch (NumberFormatException e) {
                enchant.price.set(key, null);
            }
        }
    });

    private BiFunction<Integer, ConfigurationSection, Integer> function;
    private Consumer<EnchantmentBase> setup;

    PriceIncreaseTypes(BiFunction<Integer, ConfigurationSection, Integer> function, Consumer<EnchantmentBase> setup) {
        this.function = function;
        this.setup = setup;
    }

    public long getPrice(int level, ConfigurationSection section) {
        return function.apply(level, section);
    }

    public void loadConfig(EnchantmentBase base) {
        setup.accept(base);
    }
}
