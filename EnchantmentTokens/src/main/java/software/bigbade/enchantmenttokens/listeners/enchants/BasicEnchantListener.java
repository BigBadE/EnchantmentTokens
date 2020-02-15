package software.bigbade.enchantmenttokens.listeners.enchants;

import org.bukkit.Bukkit;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;
import org.bukkit.event.Event;

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

public class BasicEnchantListener {
    private ListenerManager listeners;

    public BasicEnchantListener(ListenerManager listeners) {
        this.listeners = listeners;
    }

    public void callListeners(EnchantmentEvent event) {
        listeners.callEvent(event);
        Bukkit.getPluginManager().callEvent(event);
    }

    public void callListeners(EnchantmentEvent event, ListenerManager listeners) {
        listeners.callEvent(event);
        Bukkit.getPluginManager().callEvent(event);
    }
}
