package software.bigbade.enchantmenttokens.api;

import org.bukkit.configuration.file.FileConfiguration;

public class ExternalCurrencyData {
    private String name;
    private String displayName;

    public ExternalCurrencyData(FileConfiguration configuration) {
        name = configuration.getString("name");
        displayName = configuration.getString("displayName");
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getName() {
        return name;
    }

    public boolean matches(String name) {
        return this.name.equalsIgnoreCase(name);
    }
}
