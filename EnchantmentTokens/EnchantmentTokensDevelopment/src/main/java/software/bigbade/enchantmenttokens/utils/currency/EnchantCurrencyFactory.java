package software.bigbade.enchantmenttokens.utils.currency;

import software.bigbade.enchantmenttokens.currency.CurrencyFactory;

public abstract class EnchantCurrencyFactory implements CurrencyFactory {
    private String name;

    public EnchantCurrencyFactory(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public void shutdown() { }
}
