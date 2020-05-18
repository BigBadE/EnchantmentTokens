/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.utils.listeners;

import com.codingforcookies.armorequip.ArmorListener;
import com.codingforcookies.armorequip.DispenserArmorListener;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.EnchantListener;
import software.bigbade.enchantmenttokens.api.EnchantmentAddon;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.EventFactory;
import software.bigbade.enchantmenttokens.api.ListenerType;
import software.bigbade.enchantmenttokens.configuration.ConfigurationManager;
import software.bigbade.enchantmenttokens.configuration.ConfigurationType;
import software.bigbade.enchantmenttokens.events.EnchantmentApplyEvent;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.listeners.ChunkUnloadListener;
import software.bigbade.enchantmenttokens.listeners.GemFindListener;
import software.bigbade.enchantmenttokens.listeners.InventoryMoveListener;
import software.bigbade.enchantmenttokens.listeners.PlayerJoinListener;
import software.bigbade.enchantmenttokens.listeners.PlayerLeaveListener;
import software.bigbade.enchantmenttokens.listeners.SignClickListener;
import software.bigbade.enchantmenttokens.listeners.SignPlaceListener;
import software.bigbade.enchantmenttokens.listeners.enchants.ArmorEquipListener;
import software.bigbade.enchantmenttokens.listeners.enchants.BlockBreakListener;
import software.bigbade.enchantmenttokens.listeners.enchants.BlockDamageListener;
import software.bigbade.enchantmenttokens.listeners.enchants.PlayerDamageListener;
import software.bigbade.enchantmenttokens.listeners.enchants.PlayerDeathListener;
import software.bigbade.enchantmenttokens.listeners.enchants.PotionListener;
import software.bigbade.enchantmenttokens.listeners.enchants.ProjectileHitListener;
import software.bigbade.enchantmenttokens.listeners.enchants.ProjectileShootListener;
import software.bigbade.enchantmenttokens.listeners.enchants.RiptideListener;
import software.bigbade.enchantmenttokens.listeners.gui.EnchantmentGUIListener;
import software.bigbade.enchantmenttokens.utils.ReflectionManager;
import software.bigbade.enchantmenttokens.utils.currency.VaultCurrencyFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

public class EnchantListenerHandler implements ListenerHandler {
    private final Map<ListenerType, ListenerManager<?>> enchantListeners = new ConcurrentHashMap<>();
    private final EnchantmentTokens main;

    private ListenerManager<EnchantmentApplyEvent> enchantListener;

    private static final String FOLDER = "\\enchantments\\";

    public EnchantListenerHandler(EnchantmentTokens main) {
        EnchantmentTokens.getEnchantLogger().log(Level.INFO, "Looking for enchantments");
        for (ListenerType type : ListenerType.values()) {
            enchantListeners.put(type, new ListenerManager<>());
        }
        this.main = main;
    }

    @SuppressWarnings("unchecked")
    public void registerListeners() {
        enchantListener = (ListenerManager<EnchantmentApplyEvent>) enchantListeners.get(ListenerType.ENCHANT);

        Bukkit.getPluginManager().registerEvents(new ArmorListener(new ArrayList<>()), main);
        Bukkit.getPluginManager().registerEvents(new DispenserArmorListener(), main);

        Bukkit.getPluginManager().registerEvents(new SignPlaceListener(main.getEnchantmentHandler(), main.getPlayerHandler()), main);
        Bukkit.getPluginManager().registerEvents(new SignClickListener(main.getUtils()), main);

        Bukkit.getPluginManager().registerEvents(new EnchantmentGUIListener(main.getPlayerHandler(), main.getScheduler()), main);

        Bukkit.getPluginManager().registerEvents(new ChunkUnloadListener(main.getSignHandler().getSigns()), main);
        Bukkit.getPluginManager().registerEvents(new PlayerLeaveListener(main.getPlayerHandler()), main);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(main.getPlayerHandler()), main);

        if (main.getVersion() >= 13)
            Bukkit.getPluginManager().registerEvents(new RiptideListener(enchantListeners.get(ListenerType.RIPTIDE)), main);
        Bukkit.getPluginManager().registerEvents(new ProjectileHitListener(main.getVersion(), enchantListeners.get(ListenerType.TRIDENT_HIT), enchantListeners.get(ListenerType.ARROW_HIT)), main);
        Bukkit.getPluginManager().registerEvents(new ProjectileShootListener(main, enchantListeners.get(ListenerType.TRIDENT_THROW), enchantListeners.get(ListenerType.BOW_SHOOT), enchantListeners.get(ListenerType.CROSSBOW_SHOOT)), main);
        Bukkit.getPluginManager().registerEvents(new PlayerDamageListener(enchantListeners.get(ListenerType.ON_DAMAGED), enchantListeners.get(ListenerType.ENTITY_DAMAGED), enchantListeners.get(ListenerType.SHIELD_BLOCK)), main);
        Bukkit.getPluginManager().registerEvents(new PotionListener(enchantListeners.get(ListenerType.POTION_APPLY), enchantListeners.get(ListenerType.POTION_REMOVE)), main);

        registerBlockBreak();
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(enchantListeners.get(ListenerType.DEATH)), main);
        Bukkit.getPluginManager().registerEvents(new ArmorEquipListener(enchantListeners.get(ListenerType.EQUIP), enchantListeners.get(ListenerType.UNEQUIP)), main);
        Bukkit.getPluginManager().registerEvents(new BlockDamageListener(enchantListeners.get(ListenerType.BLOCK_DAMAGED)), main);
        Bukkit.getPluginManager().registerEvents(new InventoryMoveListener(enchantListeners.get(ListenerType.HELD), enchantListeners.get(ListenerType.SWAPPED), main.getSignHandler().getSigns(), main.getScheduler()), main);
    }

    private void registerBlockBreak() {
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(enchantListeners.get(ListenerType.BLOCK_BREAK), main.getSignHandler()), main);
        if (!(main.getCurrencyHandler() instanceof VaultCurrencyFactory)) {
            Bukkit.getPluginManager().registerEvents(new GemFindListener(main.getConfig().getConfigurationSection("currency"), main.getPlayerHandler()), main);
        }
    }

    public void onEnchant(ItemStack item, EnchantmentBase base, Player player) {
        EnchantmentApplyEvent event = new EnchantmentApplyEvent(item, player, base);
        EnchantmentEvent<EnchantmentApplyEvent> enchantmentEvent = new EventFactory<EnchantmentApplyEvent>().createEvent(event, ListenerType.ENCHANT, item, player);
        enchantListener.callEvent(enchantmentEvent, base.getEnchantment());
    }

    public void loadAddons(Collection<EnchantmentAddon> addons) {
        for (EnchantmentAddon addon : addons) {
            FileConfiguration configuration = ConfigurationManager.loadConfigurationFile(new File(main.getDataFolder().getAbsolutePath() + FOLDER + addon.getName() + ".yml"));

            for (Field field : addon.getClass().getDeclaredFields()) {
                ConfigurationManager.loadConfigForField(field, configuration, addon);
            }

            addon.onEnable();
            main.getMenuFactory().addButtons(addon.getButtons());
        }
    }

    public void loadEnchantments(Map<EnchantmentAddon, Set<Class<EnchantmentBase>>> enchants) {
        long startTime = System.currentTimeMillis();
        ConcurrentLinkedQueue<EnchantmentBase> enchantments = new ConcurrentLinkedQueue<>();

        Map<EnchantmentAddon, FileConfiguration> configs = new HashMap<>();

        enchants.keySet().forEach(addon -> configs.put(addon, ConfigurationManager.loadConfigurationFile(new File(main.getDataFolder().getAbsolutePath() + FOLDER + addon.getName() + ".yml"))));

        enchants.forEach((addon, classes) -> {
            for (Class<EnchantmentBase> clazz : classes) {
                EnchantmentBase enchant = loadClass(clazz, configs.get(addon), addon);
                if (enchant == null) continue;
                enchant.loadConfig();
                enchantments.add(enchant);
                checkMethods(enchant, clazz);
            }
        });

        Bukkit.getScheduler().runTask(main, () -> {
            registerListeners();
            main.getEnchantmentHandler().registerEnchants(enchantments);
            main.saveConfig();
        });

        for (Map.Entry<EnchantmentAddon, FileConfiguration> configuration : configs.entrySet()) {
            ConfigurationManager.saveConfiguration(new File(main.getDataFolder().getAbsolutePath() + FOLDER + configuration.getKey().getName() + ".yml"), configuration.getValue());
        }
        EnchantmentTokens.getEnchantLogger().log(Level.INFO, "Finishing loading enchantments in {0} milliseconds", System.currentTimeMillis() - startTime);
    }

    public ListenerManager<?> getListenerManager(ListenerType type) {
        return enchantListeners.get(type);
    }

    private void checkMethods(@Nonnull EnchantmentBase enchant, Class<?> clazz) {
        for (Method method : clazz.getMethods()) {
            if (!method.isAnnotationPresent(EnchantListener.class))
                continue;
            ListenerType type = method.getAnnotation(EnchantListener.class).type();
            if (canEnchant(enchant, type)) {
                enchantListeners.get(type).add(event -> ReflectionManager.invoke(method, enchant, event), enchant.getEnchantment());
            }
        }
    }

    private boolean canEnchant(EnchantmentBase enchant, ListenerType type) {
        if(enchant.getTarget() == null) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "No target set for enchantment {0}", enchant.getEnchantmentName());
            return false;
        }
        return type.canTarget(enchant.getTarget());
    }

    @Nullable
    private EnchantmentBase loadClass(Class<? extends EnchantmentBase> clazz, FileConfiguration configuration, EnchantmentAddon addon) {
        assert configuration != null;
        ConfigurationSection section = ConfigurationManager.getSectionOrCreate(configuration, "enchants");

        Constructor<?> constructor;
        try {
            constructor = clazz.getConstructor(NamespacedKey.class);
        } catch (NoSuchMethodException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "No constructor found for enchant {0}", clazz.getSimpleName());
            return null;
        }
        final EnchantmentBase enchant = (EnchantmentBase) ReflectionManager.instantiate(constructor, new NamespacedKey(addon, clazz.getSimpleName()));

        Objects.requireNonNull(enchant);

        ConfigurationSection enchantSection = ConfigurationManager.getSectionOrCreate(section, enchant.getKey().getKey());

        loadConfiguration(enchantSection, (Enchantment) enchant);

        boolean enabled = new ConfigurationType<>(true).getValue("enabled", enchantSection);

        return (enabled) ? enchant : null;
    }

    @SuppressWarnings("unchecked")
    private void loadConfiguration(ConfigurationSection section, Enchantment base) {
        Class<? extends Enchantment> currentClass = base.getClass();
        while (currentClass != Enchantment.class) {
            for (Field field : currentClass.getDeclaredFields()) {
                ConfigurationManager.loadConfigForField(field, section, base);
            }
            currentClass = (Class<? extends Enchantment>) currentClass.getSuperclass();
        }
    }
}
