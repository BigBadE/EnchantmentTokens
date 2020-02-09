package software.bigbade.enchantmenttokens.utils.currency;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import software.bigbade.enchantmenttokens.utils.EnchantLogger;

import java.util.logging.Level;

/*
EnchantmentTokens
Copyright (C) 2019-2020 Big_Bad_E
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

public class VaultCurrencyFactory implements CurrencyFactory {
    private Economy economy;

    public VaultCurrencyFactory(Server server) {
        RegisteredServiceProvider<Economy> rsp = server.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            EnchantLogger.log(Level.SEVERE, "Could not find Vault, though Vault is specified as the currency handler.");
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

    }
}
