package software.bigbade.enchantmenttokens.api;

public class EnchantmentAddon {
    private String name;

    public EnchantmentAddon(String name) {
        this.name = name;
    }

    public void onEnable() {
        //Overridden by addons
    }

    public void onDisable() {
        //Overridden by addons
    }

    public void loadConfig() {
        //Overridden by addons
    }

    public String getName() {
        return name;
    }
}
