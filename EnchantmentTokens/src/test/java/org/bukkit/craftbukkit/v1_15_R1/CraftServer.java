package org.bukkit.craftbukkit.v1_15_R1;

import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.*;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.*;
import org.bukkit.loot.LootTable;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.CachedServerIcon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class CraftServer implements Server {
    private static final String ERROR = "THIS SHOULD NOT BE USED OUTSIDE OF TESTING";
    
    @NotNull
    @Override
    public String getName() {
        return "Test";
    }

    @NotNull
    @Override
    public String getVersion() {
        return "1.15.2";
    }

    @NotNull
    @Override
    public String getBukkitVersion() {
        return "None";
    }

    @NotNull
    @Override
    public Collection<? extends Player> getOnlinePlayers() {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public int getMaxPlayers() {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public int getPort() {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public int getViewDistance() {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public String getIp() {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public String getWorldType() {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public boolean getGenerateStructures() {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public boolean getAllowEnd() {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public boolean getAllowNether() {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public boolean hasWhitelist() {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public void setWhitelist(boolean b) {

    }

    @NotNull
    @Override
    public Set<OfflinePlayer> getWhitelistedPlayers() {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public void reloadWhitelist() {

    }

    @Override
    public int broadcastMessage(@NotNull String s) {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public String getUpdateFolder() {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public File getUpdateFolderFile() {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public long getConnectionThrottle() {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public int getTicksPerAnimalSpawns() {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public int getTicksPerMonsterSpawns() {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public int getTicksPerWaterSpawns() {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public int getTicksPerAmbientSpawns() {
        throw new IllegalArgumentException(ERROR);
    }

    @Nullable
    @Override
    public Player getPlayer(@NotNull String s) {
        throw new IllegalArgumentException(ERROR);
    }

    @Nullable
    @Override
    public Player getPlayerExact(@NotNull String s) {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public List<Player> matchPlayer(@NotNull String s) {
        throw new IllegalArgumentException(ERROR);
    }

    @Nullable
    @Override
    public Player getPlayer(@NotNull UUID uuid) {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public PluginManager getPluginManager() {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public BukkitScheduler getScheduler() {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public ServicesManager getServicesManager() {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public List<World> getWorlds() {
        throw new IllegalArgumentException(ERROR);
    }

    @Nullable
    @Override
    public World createWorld(@NotNull WorldCreator worldCreator) {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public boolean unloadWorld(@NotNull String s, boolean b) {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public boolean unloadWorld(@NotNull World world, boolean b) {
        throw new IllegalArgumentException(ERROR);
    }

    @Nullable
    @Override
    public World getWorld(@NotNull String s) {
        throw new IllegalArgumentException(ERROR);
    }

    @Nullable
    @Override
    public World getWorld(@NotNull UUID uuid) {
        throw new IllegalArgumentException(ERROR);
    }

    @Nullable
    @Override
    @Deprecated
    public MapView getMap(int i) {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public MapView createMap(@NotNull World world) {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public ItemStack createExplorerMap(@NotNull World world, @NotNull Location location, @NotNull StructureType structureType) {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public ItemStack createExplorerMap(@NotNull World world, @NotNull Location location, @NotNull StructureType structureType, int i, boolean b) {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public void reload() {

    }

    @Override
    public void reloadData() {

    }

    @NotNull
    @Override
    public Logger getLogger() {
        return Logger.getLogger("TestLogger");
    }

    @Nullable
    @Override
    public PluginCommand getPluginCommand(@NotNull String s) {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public void savePlayers() {

    }

    @Override
    public boolean dispatchCommand(@NotNull CommandSender commandSender, @NotNull String s) throws CommandException {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public boolean addRecipe(@Nullable Recipe recipe) {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public List<Recipe> getRecipesFor(@NotNull ItemStack itemStack) {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public Iterator<Recipe> recipeIterator() {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public void clearRecipes() {

    }

    @Override
    public void resetRecipes() {

    }

    @Override
    public boolean removeRecipe(@NotNull NamespacedKey namespacedKey) {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public Map<String, String[]> getCommandAliases() {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public int getSpawnRadius() {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public void setSpawnRadius(int i) {

    }

    @Override
    public boolean getOnlineMode() {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public boolean getAllowFlight() {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public boolean isHardcore() {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public void shutdown() {

    }

    @Override
    public int broadcast(@NotNull String s, @NotNull String s1) {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    @Deprecated
    public OfflinePlayer getOfflinePlayer(@NotNull String s) {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public OfflinePlayer getOfflinePlayer(@NotNull UUID uuid) {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public Set<String> getIPBans() {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public void banIP(@NotNull String s) {

    }

    @Override
    public void unbanIP(@NotNull String s) {

    }

    @NotNull
    @Override
    public Set<OfflinePlayer> getBannedPlayers() {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public BanList getBanList(@NotNull BanList.Type type) {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public Set<OfflinePlayer> getOperators() {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public GameMode getDefaultGameMode() {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public void setDefaultGameMode(@NotNull GameMode gameMode) {

    }

    @NotNull
    @Override
    public ConsoleCommandSender getConsoleSender() {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public File getWorldContainer() {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public OfflinePlayer[] getOfflinePlayers() {
        return new OfflinePlayer[0];
    }

    @NotNull
    @Override
    public Messenger getMessenger() {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public HelpMap getHelpMap() {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public Inventory createInventory(@Nullable InventoryHolder inventoryHolder, @NotNull InventoryType inventoryType) {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public Inventory createInventory(@Nullable InventoryHolder inventoryHolder, @NotNull InventoryType inventoryType, @NotNull String s) {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public Inventory createInventory(@Nullable InventoryHolder inventoryHolder, int i) throws IllegalArgumentException {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public Inventory createInventory(@Nullable InventoryHolder inventoryHolder, int i, @NotNull String s) throws IllegalArgumentException {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public Merchant createMerchant(@Nullable String s) {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public int getMonsterSpawnLimit() {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public int getAnimalSpawnLimit() {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public int getAmbientSpawnLimit() {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public boolean isPrimaryThread() {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public String getMotd() {
        throw new IllegalArgumentException(ERROR);
    }

    @Nullable
    @Override
    public String getShutdownMessage() {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public Warning.WarningState getWarningState() {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public ItemFactory getItemFactory() {
        throw new IllegalArgumentException(ERROR);
    }

    @Nullable
    @Override
    public ScoreboardManager getScoreboardManager() {
        throw new IllegalArgumentException(ERROR);
    }

    @Nullable
    @Override
    public CachedServerIcon getServerIcon() {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public CachedServerIcon loadServerIcon(@NotNull File file) {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public CachedServerIcon loadServerIcon(@NotNull BufferedImage bufferedImage) {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public void setIdleTimeout(int i) {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public int getIdleTimeout() {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public ChunkGenerator.ChunkData createChunkData(@NotNull World world) {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public BossBar createBossBar(@Nullable String s, @NotNull BarColor barColor, @NotNull BarStyle barStyle, @NotNull BarFlag... barFlags) {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public KeyedBossBar createBossBar(@NotNull NamespacedKey namespacedKey, @Nullable String s, @NotNull BarColor barColor, @NotNull BarStyle barStyle, @NotNull BarFlag... barFlags) {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public Iterator<KeyedBossBar> getBossBars() {
        throw new IllegalArgumentException(ERROR);
    }

    @Nullable
    @Override
    public KeyedBossBar getBossBar(@NotNull NamespacedKey namespacedKey) {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public boolean removeBossBar(@NotNull NamespacedKey namespacedKey) {
        throw new IllegalArgumentException(ERROR);
    }

    @Nullable
    @Override
    public Entity getEntity(@NotNull UUID uuid) {
        throw new IllegalArgumentException(ERROR);
    }

    @Nullable
    @Override
    public Advancement getAdvancement(@NotNull NamespacedKey namespacedKey) {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public Iterator<Advancement> advancementIterator() {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public BlockData createBlockData(@NotNull Material material) {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public BlockData createBlockData(@NotNull Material material, @Nullable Consumer<BlockData> consumer) {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public BlockData createBlockData(@NotNull String s) throws IllegalArgumentException {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public BlockData createBlockData(@Nullable Material material, @Nullable String s) throws IllegalArgumentException {
        throw new IllegalArgumentException(ERROR);
    }

    @Nullable
    @Override
    public <T extends Keyed> Tag<T> getTag(@NotNull String s, @NotNull NamespacedKey namespacedKey, @NotNull Class<T> aClass) {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public <T extends Keyed> Iterable<Tag<T>> getTags(@NotNull String s, @NotNull Class<T> aClass) {
        throw new IllegalArgumentException(ERROR);
    }

    @Nullable
    @Override
    public LootTable getLootTable(@NotNull NamespacedKey namespacedKey) {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public List<Entity> selectEntities(@NotNull CommandSender commandSender, @NotNull String s) throws IllegalArgumentException {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    @Deprecated
    public UnsafeValues getUnsafe() {
        throw new IllegalArgumentException(ERROR);
    }

    @NotNull
    @Override
    public Spigot spigot() {
        throw new IllegalArgumentException(ERROR);
    }

    @Override
    public void sendPluginMessage(@NotNull Plugin plugin, @NotNull String s, @NotNull byte[] bytes) {

    }

    @NotNull
    @Override
    public Set<String> getListeningPluginChannels() {
        throw new IllegalArgumentException(ERROR);
    }
}
