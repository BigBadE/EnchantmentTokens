package software.bigbade.enchantmenttokens.api;

import org.jetbrains.annotations.NotNull;

public interface EnchantmentBase {
    void onDisable();

    void loadConfig();

    long getDefaultPrice(int level);

    @NotNull
    String getName();
}
