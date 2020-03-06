package software.bigbade.enchantmenttokens.utils.enchants;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

public class FakePlugin implements Plugin {
    private String name;

    public FakePlugin(String namespace) {
        this.name = namespace;
    }

    @NotNull
    @Override
    public File getDataFolder() {
        throw new UnsupportedOperationException("This should not be used for real plugins");
    }

    @NotNull
    @Override
    public PluginDescriptionFile getDescription() {
        throw new UnsupportedOperationException("This should not be used for real plugins");
    }

    @NotNull
    @Override
    public FileConfiguration getConfig() {
        throw new UnsupportedOperationException("This should not be used for real plugins");
    }

    @Nullable
    @Override
    public InputStream getResource(@NotNull String s) {
        throw new UnsupportedOperationException("This should not be used for real plugins");
    }

    @Override
    public void saveConfig() {
        throw new UnsupportedOperationException("This should not be used for real plugins");
    }

    @Override
    public void saveDefaultConfig() {
        throw new UnsupportedOperationException("This should not be used for real plugins");
    }

    @Override
    public void saveResource(@NotNull String s, boolean b) {
        throw new UnsupportedOperationException("This should not be used for real plugins");
    }

    @Override
    public void reloadConfig() {
        throw new UnsupportedOperationException("This should not be used for real plugins");
    }

    @NotNull
    @Override
    public PluginLoader getPluginLoader() {
        throw new UnsupportedOperationException("This should not be used for real plugins");
    }

    @NotNull
    @Override
    public Server getServer() {
        throw new UnsupportedOperationException("This should not be used for real plugins");
    }

    @Override
    public boolean isEnabled() {
        throw new UnsupportedOperationException("This should not be used for real plugins");
    }

    @Override
    public void onDisable() {
        throw new UnsupportedOperationException("This should not be used for real plugins");
    }

    @Override
    public void onLoad() {
        throw new UnsupportedOperationException("This should not be used for real plugins");
    }

    @Override
    public void onEnable() {
        throw new UnsupportedOperationException("This should not be used for real plugins");
    }

    @Override
    public boolean isNaggable() {
        throw new UnsupportedOperationException("This should not be used for real plugins");
    }

    @Override
    public void setNaggable(boolean b) {
        throw new UnsupportedOperationException("This should not be used for real plugins");
    }

    @Nullable
    @Override
    public ChunkGenerator getDefaultWorldGenerator(@NotNull String s, @Nullable String s1) {
        throw new UnsupportedOperationException("This should not be used for real plugins");
    }

    @NotNull
    @Override
    public Logger getLogger() {
        throw new UnsupportedOperationException("This should not be used for real plugins");
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        throw new UnsupportedOperationException("This should not be used for real plugins");
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        throw new UnsupportedOperationException("This should not be used for real plugins");
    }
}
