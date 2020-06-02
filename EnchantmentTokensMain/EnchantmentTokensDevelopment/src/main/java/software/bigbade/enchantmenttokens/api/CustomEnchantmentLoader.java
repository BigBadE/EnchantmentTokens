package software.bigbade.enchantmenttokens.api;

import co.aikar.taskchain.TaskChain;
import lombok.RequiredArgsConstructor;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.Plugin;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.configuration.ConfigurationManager;
import software.bigbade.enchantmenttokens.configuration.ConfigurationType;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.utils.ReflectionManager;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentLoader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

@RequiredArgsConstructor
public class CustomEnchantmentLoader implements EnchantmentLoader {
    private static final String FOLDER = "\\enchantments\\";
    private final EnchantmentTokens main;

    @Override
    public void loadEnchantments(Map<EnchantmentAddon, Set<Class<EnchantmentBase>>> enchants) {
        long startTime = System.currentTimeMillis();
        ConcurrentLinkedQueue<EnchantmentBase> enchantments = new ConcurrentLinkedQueue<>();

        Map<EnchantmentAddon, FileConfiguration> configs = new HashMap<>();

        enchants.keySet().forEach(addon -> configs.put(addon, ConfigurationManager.loadConfigurationFile(new File(main.getDataFolder().getAbsolutePath() + FOLDER + addon.getName() + ".yml"))));

        TaskChain<?> chain = EnchantmentTokens.newChain();

        enchants.forEach((addon, classes) -> chain.async(() -> {
            for (Class<EnchantmentBase> clazz : classes) {
                EnchantmentBase enchant = loadClass(clazz, configs.get(addon), addon);
                if (enchant == null) {
                    continue;
                }
                enchant.loadConfig();
                enchantments.add(enchant);
                checkMethods(enchant, clazz);
            }
        }));

        chain.sync(() -> {
            main.getEnchantmentHandler().registerEnchants(enchantments);
            main.getListenerHandler().registerListeners();
            main.saveConfig();
        });

        chain.execute();

        for (Map.Entry<EnchantmentAddon, FileConfiguration> configuration : configs.entrySet()) {
            ConfigurationManager.saveConfiguration(new File(main.getDataFolder().getAbsolutePath() + FOLDER + configuration.getKey().getName() + ".yml"), configuration.getValue());
        }
        EnchantmentTokens.getEnchantLogger().log(Level.INFO, "Finishing loading enchantments in {0} milliseconds", System.currentTimeMillis() - startTime);
    }

    @Override
    public void loadAddon(EnchantmentAddon addon) {
        FileConfiguration configuration = ConfigurationManager.loadConfigurationFile(new File(main.getDataFolder().getAbsolutePath() + FOLDER + addon.getName() + ".yml"));

        for (Field field : addon.getClass().getDeclaredFields()) {
            ConfigurationManager.loadConfigForField(field, configuration, addon);
        }

        addon.onEnable();
        main.getMenuFactory().addButtons(addon.getButtons());
    }

    @Override
    public void loadEnchantment(Plugin plugin, Class<? extends EnchantmentBase> clazz) {
        EnchantmentBase enchant = loadClass(clazz, plugin.getConfig(), plugin);
        if(enchant == null) {
            return;
        }
        enchant.loadConfig();
        checkMethods(enchant, clazz);
        main.getEnchantmentHandler().registerEnchant(enchant);
    }

    private void checkMethods(@Nonnull EnchantmentBase enchant, Class<?> clazz) {
        for (Method method : clazz.getMethods()) {
            if (!method.isAnnotationPresent(EnchantListener.class)) {
                continue;
            }
            ListenerType type = method.getAnnotation(EnchantListener.class).type();
            Class<?>[] params = method.getParameterTypes();
            if(params.length != 1 || !EnchantmentEvent.class.isAssignableFrom(params[0])) {
                EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Enchantment {0} has an invalid listener {1}", new Object[] { enchant.getEnchantmentName(), method.getName() });
                continue;
            }
            if (canEnchant(enchant, type)) {
                main.getListenerHandler().getListenerManager(type).add(event -> ReflectionManager.invoke(method, enchant, event), enchant.getEnchantment());
            }
        }
    }

    private boolean canEnchant(EnchantmentBase enchant, ListenerType type) {
        if (enchant.getTarget() == null) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "No target set for enchantment {0}", enchant.getEnchantmentName());
            return false;
        }
        return type.canTarget(enchant.getTarget());
    }

    @Nullable
    private EnchantmentBase loadClass(Class<? extends EnchantmentBase> clazz, FileConfiguration configuration, Plugin addon) {
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
