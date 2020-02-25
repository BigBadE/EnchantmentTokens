package software.bigbade.enchantmenttokens.utils.currency;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import software.bigbade.enchantmenttokens.utils.EnchantLogger;

import java.util.logging.Level;

public class VaultCurrencyFactory implements CurrencyFactory {
    private Economy economy;
    private boolean loaded = true;

    public VaultCurrencyFactory(Server server) {
        RegisteredServiceProvider<Economy> rsp = server.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            EnchantLogger.log(Level.SEVERE, "Could not find Vault, though Vault is specified as the currency handler.");
            loaded = false;
            return;
        }
        economy = rsp.getProvider();
    }
    @Override
    public CurrencyHandler newInstance(Player player) {
        return new VaultCurrencyHandler(player, economy);
    }

    @Override
    public String name() {
        return "vault";
    }

    @Override
    public void shutdown() {
        //Not used, handled by Vault.
    }

    @Override
    public boolean loaded() {
        return loaded;
    }
}
