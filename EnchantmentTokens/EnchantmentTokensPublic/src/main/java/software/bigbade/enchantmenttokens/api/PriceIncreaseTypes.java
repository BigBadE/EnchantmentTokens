package software.bigbade.enchantmenttokens.api;

import org.bukkit.configuration.ConfigurationSection;
import software.bigbade.enchantmenttokens.configuration.ConfigurationType;
import software.bigbade.enchantmenttokens.utils.math.AlgebraicCalculator;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public enum PriceIncreaseTypes {
    CUSTOM((level, section) ->
            section.getLong(level + ""), enchant -> {
        for (int i = enchant.getStartLevel(); i < enchant.getMaxLevel() + 1; i++) {
            if (enchant.getPrice().get(i + "") == null) {
                enchant.getPrice().set(i + "", i * 10);
            }
        }
        for (String key : enchant.getPrice().getKeys(true)) {
            try {
                if (!key.equals("type") && (Integer.parseInt(key) < enchant.getStartLevel() || Integer.parseInt(key) > enchant.getMaxLevel() + 1)) {
                    enchant.getPrice().set(key, null);
                }
            } catch (NumberFormatException e) {
                enchant.getPrice().set(key, null);
            }
        }
    }),
    LINEAR((level, section) -> level * new ConfigurationType<>(10L).getValue(StringUtils.INCREASE, section),
            enchant ->
                    new ConfigurationType<>(10).getValue("increase", enchant.getPrice())),
    ALGEBRAIC((level, section) -> AlgebraicCalculator.getInstance().getPrice(level), enchant -> new AlgebraicCalculator(new ConfigurationType<>("x^2+x-2").getValue("equation", enchant.getPrice())));

    private BiFunction<Integer, ConfigurationSection, Long> function;
    private Consumer<EnchantmentBase> setup;

    PriceIncreaseTypes(BiFunction<Integer, ConfigurationSection, Long> function, Consumer<EnchantmentBase> setup) {
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
