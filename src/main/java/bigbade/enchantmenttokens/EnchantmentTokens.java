package bigbade.enchantmenttokens;

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

import bigbade.enchantmenttokens.api.*;
import bigbade.enchantmenttokens.commands.*;
import bigbade.enchantmenttokens.gui.EnchantPickerGUI;
import bigbade.enchantmenttokens.listeners.InventoryMoveListener;
import bigbade.enchantmenttokens.listeners.SignClickListener;
import bigbade.enchantmenttokens.listeners.SignPlaceListener;
import bigbade.enchantmenttokens.listeners.enchants.BlockBreakListener;
import bigbade.enchantmenttokens.listeners.enchants.BlockDamageListener;
import bigbade.enchantmenttokens.listeners.enchants.ItemEquipListener;
import bigbade.enchantmenttokens.listeners.gui.EnchantmentGUIListener;
import bigbade.enchantmenttokens.loader.FileLoader;
import com.codingforcookies.armorequip.ArmorListener;
import com.codingforcookies.armorequip.DispenserArmorListener;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.nbt.NbtBase;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

//import org.bstats.bukkit.Metrics;

public class EnchantmentTokens extends JavaPlugin {
    public Map<String, Set<Class<EnchantmentBase>>> enchants;
    public List<EnchantmentBase> enchantments = new ArrayList<>();
    public List<VanillaEnchant> vanillaEnchants = new ArrayList<>();
    private Map<ListenerType, Map<EnchantmentBase, Method>> enchantListeners = new HashMap<>();
    private EnchantPickerGUI enchantPickerGUI;
    public static Logger LOGGER;

    private EnchantMenuCmd menuCmd;
    public FileLoader fileLoader;

    public EnchantmentLoader loader;
    public Set<Location> signs = new HashSet<>();

    private int version;

    @Override
    public void onEnable() {
        LOGGER = getLogger();
        getConfig().options().copyHeader(true).header("#Add all vanilla enchantments used in here!\nCheck configurationguide.txt for names/versions.");
        saveDefaultConfig();
        version = Integer.parseInt(Bukkit.getVersion().split("\\.")[1]);

        if (!getConfig().isSet("metrics"))
            getConfig().set("metrics", true);
        boolean metrics = getConfig().getBoolean("metics");
        if (metrics) {
            //Metrics metrics = new Metrics(this);
        }
        File configGuide = new File(getDataFolder().getPath() + "/configurationguide.txt");
        if (!configGuide.exists()) {
            Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                try {
                    InputStream stream = Objects.requireNonNull(getResource("configurationguide.txt"));
                    byte[] data = new byte[stream.available()];
                    Files.write(configGuide.toPath(), data);
                } catch (IOException e) {
                    getLogger().log(Level.SEVERE, "Could not create new configurationguide file!");
                }
            });
        }
        fileLoader = new FileLoader(this);
        if (!getDataFolder().exists())
            if (!getDataFolder().mkdir())
                getLogger().severe("[ERROR] COULD NOT CREATE DATA FOLDER. REPORT THIS, NOT THE NULLPOINTEREXCEPTION.");
        File data = new File(getDataFolder().getPath() + "\\data");
        if (!data.exists())
            if (!data.mkdir())
                getLogger().warning("[ERROR] Could not create folder " + getDataFolder().getPath() + "\\data");
        getLogger().info("Looking for enchantments");
        File enchantFolder = new File(getDataFolder().getPath() + "\\enchantments");
        if (!enchantFolder.exists())
            if (!enchantFolder.mkdir())
                getLogger().warning("[ERROR] Could not create folder enchantments at " + enchantFolder.getPath());
        loader = new EnchantmentLoader(enchantFolder, getLogger());
        enchants = loader.getEnchantments();
        getLogger().info("Registering enchants");
        registerEnchants();
        if (Bukkit.getPluginManager().isPluginEnabled(this)) {
            enchantPickerGUI = new EnchantPickerGUI(this);
            registerCommands();
            registerListeners();
        }
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        getLogger().info("Registering sign listener");
        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.MAP_CHUNK) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer container = event.getPacket();
                List<NbtBase<?>> compounds = container.getListNbtModifier().read(0);
                for (int i = 0; i < compounds.size(); i++) {
                    NbtCompound compound = (NbtCompound) compounds.get(i);
                    if (compound.getString("id").equals("minecraft:sign")) {
                        List<String> text = new ArrayList<>();
                        for (int i2 = 1; i2 < 5; i2++) {
                            try {
                                text.add(compound.getString("Text" + i2).split("text\":\"")[1].split("\"}")[0]);
                            } catch (IndexOutOfBoundsException ignored) {
                            }
                        }
                        if (text.size() != 2) return;
                        if (text.get(0).equals("[Enchantment]")) {
                            for (EnchantmentBase base1 : enchantments) {
                                if (base1.getName().equals(text.get(1))) {
                                    signs.add(new Location(event.getPlayer().getWorld(), compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z")));
                                    String price = "N/A";
                                    ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();
                                    if (base1.canEnchantItem(itemStack)) {
                                        int level = 0;
                                        for (Map.Entry<Enchantment, Integer> enchats : itemStack.getEnchantments().entrySet())
                                            if (enchats.getKey().getKey().equals(base1.getKey())) {
                                                level = enchats.getValue() + 1;
                                                break;
                                            }
                                        if (level <= base1.getMaxLevel())
                                            price = base1.getDefaultPrice(level) + "G";
                                        else
                                            price = "Maxed!";
                                    }
                                    compound.put("Text3", "{\"extra\":[{\"text\":\"" + price + "\"}],\"text\":\"\"}");
                                    compounds.set(i, compound);
                                    break;
                                }
                            }
                        }
                    }
                }
                container.getListNbtModifier().write(0, compounds);
            }
        });
        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.TILE_ENTITY_DATA) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer container = event.getPacket();
                if (container.getIntegers().getValues().get(0) == 9) {
                    NbtCompound base = (NbtCompound) container.getNbtModifier().read(0);
                    List<String> text = new ArrayList<>();
                    for (int i = 1; i < 5; i++) {
                        try {
                            text.add(base.getString("Text" + i).split("text\":\"")[1].split("\"}")[0]);
                        } catch (IndexOutOfBoundsException ignored) {
                        }
                    }
                    if (text.size() != 2) return;
                    if (text.get(0).equals("[Enchantment]")) {
                        for (EnchantmentBase base1 : enchantments) {
                            if (base1.getName().equals(text.get(1))) {
                                signs.add(new Location(event.getPlayer().getWorld(), base.getInteger("x"), base.getInteger("y"), base.getInteger("z")));
                                String price = "N/A";
                                ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();
                                if (base1.canEnchantItem(itemStack)) {
                                    int level = base1.getStartLevel();
                                    for (Map.Entry<Enchantment, Integer> enchants : itemStack.getEnchantments().entrySet())
                                        if (enchants.getKey().getKey().equals(base1.getKey())) {
                                            level = enchants.getValue();
                                            break;
                                        }
                                    if (level < base1.getMaxLevel())
                                        price = base1.getDefaultPrice(level + 1) + "G";
                                    else
                                        price = "Maxed!";
                                }
                                base.put("Text3", "{\"extra\":[{\"text\":\"Price: " + price + "\"}],\"text\":\"\"}");
                                container.getNbtModifier().write(0, base);
                                break;
                            }
                        }
                    }
                }
            }
        });
        for (EnchantmentAddon addon : loader.getAddons()) {
            File config = new File(getDataFolder().getPath() + "\\enchantments\\" + addon.getName() + ".yml");

            FileConfiguration configuration = new YamlConfiguration();
            try {
                if (!config.exists())
                    if (!config.createNewFile())
                        getLogger().log(Level.SEVERE, "Could not make new enchantment configuration folder, make sure the folder is accessible by programs");
                configuration.load(config);
            } catch (IOException | InvalidConfigurationException e) {
                getLogger().log(Level.SEVERE, "could not load enchantment configuration", e);
            }

            for (Field field : addon.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(ConfigurationField.class)) {
                    try {
                        field.setAccessible(true);
                        field.set(addon, configuration.get(field.getName()));
                    } catch (NullPointerException | IllegalAccessException e) {
                        if (e instanceof NullPointerException) {
                            try {
                                configuration.set(field.getName(), field.get(addon));
                            } catch (IllegalAccessException ex) {
                                getLogger().log(Level.SEVERE, "Configuration field set to final");
                            }
                        }
                    }
                }
            }
        }
        loader.getAddons().forEach(EnchantmentAddon::onEnable);
    }

    @Override
    public void onDisable() {
        fileLoader.saveCache();
        saveConfig();
        loader.getAddons().forEach(EnchantmentAddon::onDisable);
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new ArmorListener(new ArrayList<>()), this);
        Bukkit.getPluginManager().registerEvents(new DispenserArmorListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(enchantListeners.get(ListenerType.BLOCKBREAK), this), this);
        Bukkit.getPluginManager().registerEvents(new ItemEquipListener(enchantListeners.get(ListenerType.EQUIP), enchantListeners.get(ListenerType.UNEQUIP)), this);
        Bukkit.getPluginManager().registerEvents(new BlockDamageListener(enchantListeners.get(ListenerType.BLOCKDAMAGED)), this);

        Bukkit.getPluginManager().registerEvents(new SignPlaceListener(this), this);
        Bukkit.getPluginManager().registerEvents(new SignClickListener(this), this);
        Bukkit.getPluginManager().registerEvents(new InventoryMoveListener(enchantListeners.get(ListenerType.HELD), enchantListeners.get(ListenerType.SWAPPED), this), this);

        Bukkit.getPluginManager().registerEvents(new EnchantmentGUIListener(this, enchantPickerGUI, menuCmd, version), this);
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("adminenchant")).setExecutor(new EnchantCmd(this));
        Objects.requireNonNull(getCommand("adminenchant")).setTabCompleter(new EnchantTabCompleter(this));

        Objects.requireNonNull(getCommand("addgems")).setExecutor(new AddGemCmd(this));
        Objects.requireNonNull(getCommand("addgems")).setTabCompleter(new AddGemTabCompleter());

        menuCmd = new EnchantMenuCmd(version);
        Objects.requireNonNull(getCommand("tokenenchant")).setExecutor(menuCmd);
        Objects.requireNonNull(getCommand("tokenenchant")).setTabCompleter(new GenericTabCompleter());

        Objects.requireNonNull(getCommand("gembal")).setExecutor(new BalanceCmd(this));
        Objects.requireNonNull(getCommand("gembal")).setTabCompleter(new GenericTabCompleter());

        Objects.requireNonNull(getCommand("enchantlist")).setExecutor(new EnchantmentList(this));
        Objects.requireNonNull(getCommand("enchantlist")).setTabCompleter(new GenericTabCompleter());

        Objects.requireNonNull(getCommand("reloadenchants")).setExecutor(new RecompileEnchantsCmd(this));
        Objects.requireNonNull(getCommand("reloadenchants")).setTabCompleter(new GenericTabCompleter());
    }

    public void unregisterEnchants() {
        Field byKey = null;
        Field byName = null;
        try {
            byKey = Enchantment.class.getDeclaredField("byKey");
            byName = Enchantment.class.getDeclaredField("byName");
        } catch (NoSuchFieldException e) {
            getLogger().severe("If you see this, blame Spigot not me.");
        }

        assert byKey != null;
        assert byName != null;
        byKey.setAccessible(true);
        byName.setAccessible(true);

        Field modifiersField = null;
        try {
            modifiersField = Field.class.getDeclaredField("modifiers");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        assert modifiersField != null;
        modifiersField.setAccessible(true);
        try {
            modifiersField.setInt(byKey, byKey.getModifiers() & ~Modifier.FINAL);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        modifiersField.setAccessible(true);
        try {
            modifiersField.setInt(byName, byName.getModifiers() & ~Modifier.FINAL);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            Map<NamespacedKey, Enchantment> byKeys = (HashMap<NamespacedKey, Enchantment>) byKey.get(null);
            for (Enchantment enchantment : enchantments) {
                byKeys.remove(enchantment.getKey());
            }
            byKey.set(null, byKeys);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            Map<NamespacedKey, String> byNames = (HashMap<NamespacedKey, String>) byKey.get(null);
            for (Enchantment enchantment : enchantments) {
                byNames.remove(enchantment.getKey());
            }
            byName.set(null, byNames);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Map<EnchantmentBase, Method> getListeners(ListenerType type) {
        return enchantListeners.get(type);
    }

    public void registerEnchants() {
        for (ListenerType type : ListenerType.values()) {
            enchantListeners.put(type, new HashMap<>());
        }

        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Map.Entry<String, Set<Class<EnchantmentBase>>> entry : enchants.entrySet()) {
            for (Class<EnchantmentBase> clazz : entry.getValue()) {
                File config = new File(getDataFolder().getPath() + "\\enchantments\\" + entry.getKey() + ".yml");

                FileConfiguration configuration = new YamlConfiguration();
                try {
                    if (!config.exists())
                        if (!config.createNewFile())
                            getLogger().log(Level.SEVERE, "Problem creating config file at " + config.getPath());
                    configuration.load(config);
                } catch (IOException | InvalidConfigurationException e) {
                    getLogger().log(Level.SEVERE, "could not load enchantment configuration", e);
                }

                ConfigurationSection section = configuration.getConfigurationSection("enchants");
                if (section == null)
                    section = configuration.createSection("enchants");
                try {
                    String name = clazz.getSimpleName();
                    ConfigurationSection enchantSection = configuration.getConfigurationSection(name);
                    final EnchantmentBase enchant = clazz.newInstance();
                    if (enchantSection == null) {
                        enchantSection = section.createSection(name.toLowerCase());
                        enchantSection.set("enabled", true);
                    }
                    for (Field field : clazz.getDeclaredFields()) {
                        loadConfigForField(field, enchantSection, enchant);
                    }
                    for (Field field : clazz.getSuperclass().getDeclaredFields()) {
                        loadConfigForField(field, enchantSection, enchant);
                    }
                    boolean enabled;
                    try {
                        enabled = enchantSection.getBoolean("enabled");
                    } catch (NullPointerException e) {
                        enabled = true;
                        enchantSection.set("enabled", true);
                    }
                    if (enabled) {
                        enchant.loadConfig();
                        enchantments.add(enchant);
                        methodCheck:
                        for (Method method : clazz.getDeclaredMethods()) {
                            if (method.isAnnotationPresent(EnchantListener.class)) {
                                ListenerType type = method.getAnnotation(EnchantListener.class).type();
                                if (enchant.getItemTarget() != null) {
                                    if (!type.canTarget(enchant.getItemTarget())) {
                                        getLogger().warning("Cannot add listener " + type + " to target " + enchant.getItemTarget());
                                        continue;
                                    }
                                } else
                                    for (Material material : enchant.getTargets()) {
                                        if (!type.canTarget(material)) {
                                            getLogger().warning("Cannot add listener " + type + " to target " + material);
                                        }
                                        continue methodCheck;
                                    }
                                enchantListeners.get(type).put(enchant, method);
                            }
                        }
                        Enchantment.registerEnchantment(enchant);
                    }
                } catch (IllegalArgumentException | InstantiationException | IllegalAccessException e) {
                    if (e instanceof IllegalAccessException) {
                        getLogger().log(Level.SEVERE, "Enchantment class " + clazz.getSimpleName() + "is set to private/Configuration field is set to final!");
                    } else if (e instanceof InstantiationException) {
                        getLogger().log(Level.SEVERE, "Could not create new enchantment " + clazz.getSimpleName() + ", make sure your have no constructor arguments");
                    } else {
                        getLogger().log(Level.SEVERE, "Incorrect arguments on enchantment " + clazz.getSimpleName());
                    }
                    getLogger().log(Level.SEVERE, "Failed to register! Please do not report this to BigBadE, report this to the creator of " + clazz.getSimpleName(), e);
                    return;
                }
                try {
                    if (config.exists() && !config.delete())
                        getLogger().log(Level.SEVERE, "Could not save configuration");
                    Files.write(config.toPath(), configuration.saveToString().getBytes());
                } catch (IOException e) {
                    getLogger().log(Level.SEVERE, "Could not save configuration", e);
                }
            }
        }
        List<Enchantment> enchantsToRegister = new ArrayList<>();
        ConfigurationSection section = getConfig().getConfigurationSection("enchants");
        if (section == null)
            section = getConfig().createSection("enchants");

        for (String name : section.getStringList("vanillaEnchants")) {
            Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(name.toLowerCase().replace(" ", "_")));
            if (enchantment != null) enchantsToRegister.add(enchantment);
            else getLogger().log(Level.SEVERE, "Could not find an enchantment by the name " + name);
        }

        for (Enchantment enchantment : enchantsToRegister) {
            ConfigurationSection enchantSection = section.getConfigurationSection(enchantment.getKey().getKey());
            if (enchantSection == null)
                enchantSection = section.createSection(enchantment.getKey().getKey());
            String iconName = enchantSection.getString("icon");
            if (iconName == null)
                iconName = "BEDROCK";
            Material icon = Material.getMaterial(iconName);

            try {
                if (enchantSection.getBoolean("enabled"))
                    vanillaEnchants.add(new VanillaEnchant(icon, enchantment));
            } catch (NullPointerException e) {
                enchantSection.set("enabled", true);
                vanillaEnchants.add(new VanillaEnchant(icon, enchantment));
            }
        }

        saveConfig();

        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(false);
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Could not set enchantments to registered", e);
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private void loadConfigForField(Field field, ConfigurationSection section, Object target) {
        if (field.isAnnotationPresent(ConfigurationField.class)) {
            String location = field.getAnnotation(ConfigurationField.class).value();
            ConfigurationSection current = section;
            if (location.contains(".")) {
                String[] next = location.split("\\.");
                for (String nextLoc : next) {
                    try {
                        ConfigurationSection newSection = Objects.requireNonNull(current).getConfigurationSection(nextLoc);
                        if (newSection == null)
                            current = current.createSection(nextLoc);
                        else
                            current = newSection;
                    } catch (NullPointerException ignored) {
                        assert current != null;
                        current = current.createSection(nextLoc);
                    }
                }
            }
            location = field.getName();
            try {
                field.setAccessible(true);
                if (field.getType().equals(ConfigurationSection.class)) {
                    System.out.println(current.getCurrentPath());
                    ConfigurationSection newSection = current.getConfigurationSection(location);
                    if (newSection == null)
                        newSection = current.createSection(location);
                    field.set(target, newSection);
                } else {
                    Object value = Objects.requireNonNull(current).get(location);
                    if (value == null)
                        if (field.getType().equals(ConfigurationSection.class))
                            field.set(target, current.createSection(location));
                        else
                            current.set(location, field.get(target));
                    else
                        field.set(target, value);
                }
            } catch (NullPointerException | IllegalAccessException e) {
                if (e instanceof NullPointerException) {
                    field.setAccessible(true);
                    try {
                        Objects.requireNonNull(current).set(location, field.get(target));
                    } catch (IllegalAccessException ex) {
                        getLogger().log(Level.SEVERE, "Could not access configuration field", e);
                    }
                } else
                    getLogger().log(Level.SEVERE, "Could not access configuration field", e);
            }
        }
    }
}
