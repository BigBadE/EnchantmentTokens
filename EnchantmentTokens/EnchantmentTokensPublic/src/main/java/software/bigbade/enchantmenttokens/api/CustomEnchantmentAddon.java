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

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import software.bigbade.enchantmenttokens.EnchantmentTokens;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

public class CustomEnchantmentAddon implements EnchantmentAddon {
    private File folder;
    private PluginDescriptionFile pluginFile;

    private static final String NOT_USED = "This method should NOT be called by Addons, check the developer guide for proper use!";

    public final void setup(EnchantmentTokens main, PluginDescriptionFile file) {
        folder = main.getEnchantmentFolder();
        pluginFile = file;
    }

    @Override
    public void onEnable() {
        //Overridden by subclasses
    }

    @Override
    public void onDisable() {
        //Overridden by subclasses
    }

    @Nonnull
    @Override
    public File getDataFolder() {
        return folder;
    }

    @Nonnull
    @Override
    public PluginDescriptionFile getDescription() {
        return pluginFile;
    }

    @Nonnull
    @Override
    public FileConfiguration getConfig() {
        throw new UnsupportedOperationException(NOT_USED);
    }

    @Nullable
    @Override
    public InputStream getResource(@Nonnull String s) {
        throw new UnsupportedOperationException(NOT_USED);
    }

    @Override
    public void saveConfig() {
        throw new UnsupportedOperationException(NOT_USED);
    }

    @Override
    public void saveDefaultConfig() {
        throw new UnsupportedOperationException(NOT_USED);
    }

    @Override
    public void saveResource(@Nonnull String s, boolean b) {
        throw new UnsupportedOperationException(NOT_USED);
    }

    @Override
    public void reloadConfig() {
        throw new UnsupportedOperationException(NOT_USED);
    }

    @Nonnull
    @Override
    public PluginLoader getPluginLoader() {
        throw new UnsupportedOperationException(NOT_USED);
    }

    @Nonnull
    @Override
    public Server getServer() {
        return Bukkit.getServer();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void onLoad() {
        throw new UnsupportedOperationException(NOT_USED);
    }

    @Override
    public boolean isNaggable() {
        throw new UnsupportedOperationException(NOT_USED);
    }

    @Override
    public void setNaggable(boolean b) {
        throw new UnsupportedOperationException(NOT_USED);
    }

    @Nullable
    @Override
    public ChunkGenerator getDefaultWorldGenerator(@Nonnull String s, @Nullable String s1) {
        throw new UnsupportedOperationException(NOT_USED);
    }

    @Nonnull
    @Override
    public Logger getLogger() {
        return EnchantmentTokens.getEnchantLogger();
    }

    @Nonnull
    @Override
    public String getName() {
        return pluginFile.getName();
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] strings) {
        throw new UnsupportedOperationException(NOT_USED);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] strings) {
        throw new UnsupportedOperationException(NOT_USED);
    }
}
