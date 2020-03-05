package software.bigbade.enchantmenttokens.utils.listeners;

import com.codingforcookies.armorequip.ArmorListener;
import com.codingforcookies.armorequip.DispenserArmorListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.EnchantListener;
import software.bigbade.enchantmenttokens.api.EnchantmentAddon;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.ListenerType;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.listeners.*;
import software.bigbade.enchantmenttokens.listeners.enchants.*;
import software.bigbade.enchantmenttokens.listeners.gui.EnchantmentGUIListener;
import software.bigbade.enchantmenttokens.utils.EnchantLogger;
import software.bigbade.enchantmenttokens.utils.ReflectionManager;
import software.bigbade.enchantmenttokens.utils.configuration.ConfigurationManager;
import software.bigbade.enchantmenttokens.utils.configuration.ConfigurationType;
import software.bigbade.enchantmenttokens.utils.currency.VaultCurrencyFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

public class ListenerHandler {
    private Map<ListenerType, ListenerManager> enchantListeners = new ConcurrentHashMap<>();
    private EnchantmentTokens main;

    public ListenerHandler(EnchantmentTokens main) {
        EnchantLogger.log(Level.INFO, "Looking for enchantments");
        for (ListenerType type : ListenerType.values()) {
            enchantListeners.put(type, new ListenerManager());
        }
        this.main = main;
    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new ArmorListener(new ArrayList<>()), main);
        Bukkit.getPluginManager().registerEvents(new DispenserArmorListener(), main);

        Bukkit.getPluginManager().registerEvents(new SignPlaceListener(main.getEnchantmentHandler()), main);
        Bukkit.getPluginManager().registerEvents(new SignClickListener(main.getUtils()), main);

        Bukkit.getPluginManager().registerEvents(new EnchantmentGUIListener(main.getPlayerHandler(), main.getScheduler()), main);

        Bukkit.getPluginManager().registerEvents(new ChunkUnloadListener(main.getSignHandler().getSigns()), main);
        Bukkit.getPluginManager().registerEvents(new PlayerLeaveListener(main.getPlayerHandler()), main);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(main.getPlayerHandler()), main);

        Bukkit.getPluginManager().registerEvents(new RiptideListener(enchantListeners.get(ListenerType.RIPTIDE)), main);
        Bukkit.getPluginManager().registerEvents(new ProjectileShootListener(main.getVersion(), enchantListeners.get(ListenerType.TRIDENT_THROW), enchantListeners.get(ListenerType.SHOOT)), main);
        Bukkit.getPluginManager().registerEvents(new DamageListener(enchantListeners.get(ListenerType.DAMAGE), enchantListeners.get(ListenerType.SHIELD_BLOCK)), main);
        Bukkit.getPluginManager().registerEvents(new PotionListener(enchantListeners.get(ListenerType.POTION_APPLY), enchantListeners.get(ListenerType.POTION_REMOVE)), main);

        registerBlockBreak();
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(enchantListeners.get(ListenerType.DEATH)), main);
        Bukkit.getPluginManager().registerEvents(new ArmorEquipListener(enchantListeners.get(ListenerType.EQUIP), enchantListeners.get(ListenerType.UNEQUIP)), main);
        Bukkit.getPluginManager().registerEvents(new BlockDamageListener(enchantListeners.get(ListenerType.BLOCK_DAMAGED)), main);
        Bukkit.getPluginManager().registerEvents(new InventoryMoveListener(enchantListeners.get(ListenerType.HELD), enchantListeners.get(ListenerType.SWAPPED), main.getSignHandler().getSigns(), main.getScheduler()), main);
    }

    private void registerBlockBreak() {
        if(main.getCurrencyHandler() instanceof VaultCurrencyFactory) {
            Bukkit.getPluginManager().registerEvents(new BlockBreakListener(enchantListeners.get(ListenerType.BLOCK_BREAK), main.getSignHandler(), null, null), main);
        } else {
            Bukkit.getPluginManager().registerEvents(new BlockBreakListener(enchantListeners.get(ListenerType.BLOCK_BREAK), main.getSignHandler(), main.getConfig().getConfigurationSection("currency"), main.getPlayerHandler()), main);
        }
    }

    public void onEnchant(ItemStack item, EnchantmentBase base, Player player) {
        ListenerManager manager = enchantListeners.get(ListenerType.ENCHANT);
        EnchantmentEvent enchantmentEvent = new EnchantmentEvent(ListenerType.ENCHANT, item).setUser(player);
        manager.callEvent(enchantmentEvent, base);
    }

    public void loadAddons(Collection<EnchantmentAddon> addons) {
        for (EnchantmentAddon addon : addons) {
            FileConfiguration configuration = ConfigurationManager.loadConfigurationFile(new File(main.getDataFolder().getAbsolutePath() + "\\enchantments\\" + addon.getName() + ".yml"));

            for (Field field : addon.getClass().getDeclaredFields()) {
                ConfigurationManager.loadConfigForField(field, configuration, addon);
            }

            addon.loadConfig();
            checkMethods(null, addon.getClass());

            addon.onEnable();
        }
    }

    public void loadEnchantments(Map<String, Set<Class<EnchantmentBase>>> enchants) {
        ConcurrentLinkedQueue<EnchantmentBase> enchantments = new ConcurrentLinkedQueue<>();

        Map<String, FileConfiguration> configs = new HashMap<>();

        enchants.keySet().forEach(key -> ConfigurationManager.loadConfigurationFile(new File(main.getDataFolder().getAbsolutePath() + "\\enchantments\\" + key + ".yml")));

        enchants.forEach((addon, classes) -> {
            for (Class<EnchantmentBase> clazz : classes) {
                EnchantmentBase enchant = loadConfiguration(clazz, configs, addon);

                if (enchant == null) continue;
                enchant.loadConfig();
                enchantments.add(enchant);
                checkMethods(enchant, clazz);
            }
        });

        Bukkit.getScheduler().runTask(main, () -> {
            registerListeners();
            EnchantLogger.log(Level.INFO, "Finishing loading enchantments");
            main.getEnchantmentHandler().registerEnchants(enchantments);
            main.saveConfig();
        });

        for (Map.Entry<String, FileConfiguration> configuration : configs.entrySet()) {
            ConfigurationManager.saveConfiguration(new File(main.getDataFolder().getAbsolutePath() + "\\enchantments\\" + configuration.getKey() + ".yml"), configuration.getValue());
        }
    }

    public ListenerManager getListenerManager(ListenerType type) {
        return enchantListeners.get(type);
    }

    @SuppressWarnings("unchecked")
    private void checkMethods(EnchantmentBase enchant, Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(EnchantListener.class) || method.getReturnType() != EnchantmentListener.class)
                continue;
            ListenerType type = method.getAnnotation(EnchantListener.class).type();
            if (enchant != null && canEnchant(enchant, type)) {
                enchantListeners.get(type).add((EnchantmentListener<EnchantmentEvent>) ReflectionManager.invoke(method, enchant), enchant);
            }
        }
    }

    private boolean canEnchant(EnchantmentBase enchant, ListenerType type) {
        if (!type.canTarget(enchant.getTargets())) {
            EnchantLogger.log(Level.SEVERE, "Cannot add listener {0} to targets {1}", type, enchant.getTargets());
            return false;
        } else if (!type.canTarget(enchant.getItemTarget())) {
            EnchantLogger.log(Level.SEVERE, "Cannot add listener {0} to target {1}", type, enchant.getItemTarget());
            return false;
        }
        return true;
    }

    private EnchantmentBase loadConfiguration(Class<EnchantmentBase> clazz, Map<String, FileConfiguration> configs, String addon) {
        FileConfiguration configuration = configs.computeIfAbsent(addon, key -> null);

        assert configuration != null;
        ConfigurationSection section = ConfigurationManager.getSectionOrCreate(configuration, "enchants");

        final EnchantmentBase enchant = (EnchantmentBase) ReflectionManager.instantiate(clazz);

        ConfigurationSection enchantSection = ConfigurationManager.getSectionOrCreate(section, clazz.getSimpleName().toLowerCase());

        for (Field field : clazz.getDeclaredFields()) {
            ConfigurationManager.loadConfigForField(field, enchantSection, enchant);
        }
        for (Field field : clazz.getSuperclass().getDeclaredFields()) {
            ConfigurationManager.loadConfigForField(field, enchantSection, enchant);
        }

        boolean enabled = new ConfigurationType<>(true).getValue("enabled", enchantSection);

        return (enabled) ? enchant : null;
    }
}
