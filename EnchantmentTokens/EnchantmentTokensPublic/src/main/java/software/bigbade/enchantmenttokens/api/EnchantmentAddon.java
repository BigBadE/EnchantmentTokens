package software.bigbade.enchantmenttokens.api;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bigbade.enchantmenttokens.EnchantmentTokens;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

public class EnchantmentAddon implements Plugin {
    private File folder;
    private PluginDescriptionFile pluginFile;

    private static final String NOTUSED = "This method should NOT be called by Addons, check the developer guide for proper use!";

    public final void setup(EnchantmentTokens main, InputStream fileStream) throws InvalidDescriptionException {
        folder = main.getEnchantmentFolder();
        pluginFile = new PluginDescriptionFile(fileStream);
    }

    /**
     * Called after the configuration is loaded but before the plugin is enabled.
     */
    public void loadConfig() {
        //Overridden by subclasses
    }

    @Override
    public void onEnable() {
        //Overridden by subclasses
    }

    @Override
    public void onDisable() {
        //Overridden by subclasses
    }

    @NotNull
    @Override
    public File getDataFolder() {
        return folder;
    }

    @NotNull
    @Override
    public PluginDescriptionFile getDescription() {
        return pluginFile;
    }

    @NotNull
    @Override
    public FileConfiguration getConfig() {
        throw new UnsupportedOperationException(NOTUSED);
    }

    @Nullable
    @Override
    public InputStream getResource(@NotNull String s) {
        throw new UnsupportedOperationException(NOTUSED);
    }

    @Override
    public void saveConfig() {
        throw new UnsupportedOperationException(NOTUSED);
    }

    @Override
    public void saveDefaultConfig() {
        throw new UnsupportedOperationException(NOTUSED);
    }

    @Override
    public void saveResource(@NotNull String s, boolean b) {
        throw new UnsupportedOperationException(NOTUSED);
    }

    @Override
    public void reloadConfig() {
        throw new UnsupportedOperationException(NOTUSED);
    }

    @NotNull
    @Override
    public PluginLoader getPluginLoader() {
        throw new UnsupportedOperationException(NOTUSED);
    }

    @NotNull
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
        throw new UnsupportedOperationException(NOTUSED);
    }

    @Override
    public boolean isNaggable() {
        throw new UnsupportedOperationException(NOTUSED);
    }

    @Override
    public void setNaggable(boolean b) {
        throw new UnsupportedOperationException(NOTUSED);
    }

    @Nullable
    @Override
    public ChunkGenerator getDefaultWorldGenerator(@NotNull String s, @Nullable String s1) {
        throw new UnsupportedOperationException(NOTUSED);
    }

    @NotNull
    @Override
    public Logger getLogger() {
        return EnchantmentTokens.getEnchantLogger();
    }

    @NotNull
    @Override
    public String getName() {
        return pluginFile.getName();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        throw new UnsupportedOperationException(NOTUSED);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        throw new UnsupportedOperationException(NOTUSED);
    }
}
