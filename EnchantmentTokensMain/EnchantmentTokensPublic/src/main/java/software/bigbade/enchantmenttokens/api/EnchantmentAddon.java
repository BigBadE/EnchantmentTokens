/*
 * Addons for the Custom Enchantment API in Minecraft
 * Copyright (C) 2020 BigBadE
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package software.bigbade.enchantmenttokens.api;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.gui.EnchantButton;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

public interface EnchantmentAddon extends Plugin {
    /**
     * Used to setup the various things in the addon
     * @param main Main class of EnchantmentTokens.
     * @param file PluginDescriptionFile for the plugin.
     */
    void setup(EnchantmentTokens main, PluginDescriptionFile file);

    /**
     * All the locales supported by the addon
     * @return an array of locales, should be countries and not languages.
     */
    Locale[] supportedLocales();

    /**
     * All the buttons this addon adds, should be a multiple of 7.
     * @return All the buttons added by this addon. Return an empty collection instead of null.
     */
    @Nonnull
    List<EnchantButton> getButtons();
}
