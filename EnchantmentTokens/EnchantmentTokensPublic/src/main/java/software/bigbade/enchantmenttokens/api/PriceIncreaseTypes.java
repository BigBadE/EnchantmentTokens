package software.bigbade.enchantmenttokens.api;

import org.bukkit.configuration.ConfigurationSection;
import software.bigbade.enchantmenttokens.configuration.ConfigurationType;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public enum PriceIncreaseTypes {
    CUSTOM((level, section) ->
            section.getInt(level + ""), enchant -> {
        for (int i = enchant.getStartLevel(); i < enchant.getMaxLevel() + 1; i++) {
            if (enchant.getPriceSection().get(i + "") == null) {
                enchant.getPriceSection().set(i + "", i * 10);
            }
        }
        for (String key : enchant.getPriceSection().getKeys(true)) {
            try {
                if (!key.equals("type") && (Integer.parseInt(key) < enchant.getStartLevel() || Integer.parseInt(key) > enchant.getMaxLevel() + 1)) {
                    enchant.getPriceSection().set(key, null);
                }
            } catch (NumberFormatException e) {
                enchant.getPriceSection().set(key, null);
            }
        }
    }),
    LINEAR((level, section) -> level * new ConfigurationType<>(10).getValue(StringUtils.INCREASE, section),
            enchant -> {
                if (!enchant.getPriceSection().isSet(StringUtils.INCREASE))
                    enchant.getPriceSection().set(StringUtils.INCREASE, 10);
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
