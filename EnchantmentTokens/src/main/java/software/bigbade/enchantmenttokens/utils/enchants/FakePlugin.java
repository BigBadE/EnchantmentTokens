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

    private static final String ERROR = "This should not be used for real plugins";

    public FakePlugin(String namespace) {
        this.name = namespace;
    }

    @NotNull
    @Override
    public File getDataFolder() {
        throw new UnsupportedOperationException(ERROR);
    }

    @NotNull
    @Override
    public PluginDescriptionFile getDescription() {
        throw new UnsupportedOperationException(ERROR);
    }

    @NotNull
    @Override
    public FileConfiguration getConfig() {
        throw new UnsupportedOperationException(ERROR);
    }

    @Nullable
    @Override
    public InputStream getResource(@NotNull String s) {
        throw new UnsupportedOperationException(ERROR);
    }

    @Override
    public void saveConfig() {
        throw new UnsupportedOperationException(ERROR);
    }

    @Override
    public void saveDefaultConfig() {
        throw new UnsupportedOperationException(ERROR);
    }

    @Override
    public void saveResource(@NotNull String s, boolean b) {
        throw new UnsupportedOperationException(ERROR);
    }

    @Override
    public void reloadConfig() {
        throw new UnsupportedOperationException(ERROR);
    }

    @NotNull
    @Override
    public PluginLoader getPluginLoader() {
        throw new UnsupportedOperationException(ERROR);
    }

    @NotNull
    @Override
    public Server getServer() {
        throw new UnsupportedOperationException(ERROR);
    }

    @Override
    public boolean isEnabled() {
        throw new UnsupportedOperationException(ERROR);
    }

    @Override
    public void onDisable() {
        throw new UnsupportedOperationException(ERROR);
    }

    @Override
    public void onLoad() {
        throw new UnsupportedOperationException(ERROR);
    }

    @Override
    public void onEnable() {
        throw new UnsupportedOperationException(ERROR);
    }

    @Override
    public boolean isNaggable() {
        throw new UnsupportedOperationException(ERROR);
    }

    @Override
    public void setNaggable(boolean b) {
        throw new UnsupportedOperationException(ERROR);
    }

    @Nullable
    @Override
    public ChunkGenerator getDefaultWorldGenerator(@NotNull String s, @Nullable String s1) {
        throw new UnsupportedOperationException(ERROR);
    }

    @NotNull
    @Override
    public Logger getLogger() {
        throw new UnsupportedOperationException(ERROR);
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        throw new UnsupportedOperationException(ERROR);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        throw new UnsupportedOperationException(ERROR);
    }
}
