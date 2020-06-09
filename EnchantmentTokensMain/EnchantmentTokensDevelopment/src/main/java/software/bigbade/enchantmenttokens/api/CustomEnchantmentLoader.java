package software.bigbade.enchantmenttokens.api;

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
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentLoader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;

@RequiredArgsConstructor
public class CustomEnchantmentLoader implements EnchantmentLoader {
    private static final String FOLDER = "\\enchantments\\";
    private final EnchantmentTokens main;

    @Override
    public void loadEnchantments(EnchantmentAddon addon, EnchantmentHandler handler, Set<Class<EnchantmentBase>> enchants) {
        File configFile = new File(main.getDataFolder().getAbsolutePath() + FOLDER + addon.getName() + ".yml");
        FileConfiguration configuration = ConfigurationManager.loadConfigurationFile(configFile);
        for (Class<EnchantmentBase> enchantClass : enchants) {
            EnchantmentBase enchant = loadClass(enchantClass, configuration, addon);
            if (enchant == null) {
                continue;
            }
            enchant.loadConfig();
            for (Method method : enchantClass.getMethods()) {
                checkMethod(enchant, method);
            }
            handler.registerEnchant(enchant);
        }

        ConfigurationManager.saveConfiguration(configFile, configuration);
    }

    @Override
    public void loadEnchantment(Plugin plugin, Class<? extends EnchantmentBase> clazz) {
        EnchantmentBase enchant = loadClass(clazz, plugin.getConfig(), plugin);
        if (enchant == null) {
            return;
        }
        enchant.loadConfig();
        for (Method method : clazz.getMethods()) {
            checkMethod(enchant, method);
        }
        main.getEnchantmentHandler().registerEnchant(enchant);
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

    private void checkMethod(@Nonnull EnchantmentBase enchant, Method method) {
        if (!method.isAnnotationPresent(EnchantListener.class)) {
            return;
        }
        ListenerType type = method.getAnnotation(EnchantListener.class).type();
        Class<?>[] params = method.getParameterTypes();
        if (params.length != 1 || !EnchantmentEvent.class.isAssignableFrom(params[0])) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Enchantment {0} has an invalid listener {1}", new Object[]{enchant.getEnchantmentName(), method.getName()});
            return;
        }
        if (canEnchant(enchant, type)) {
            main.getListenerHandler().getListenerManager(type).add(event -> ReflectionManager.invoke(method, enchant, event), enchant.getEnchantment());
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

        Constructor<? extends EnchantmentBase> constructor;
        try {
            constructor = clazz.getConstructor(NamespacedKey.class);
        } catch (NoSuchMethodException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "No constructor found for enchant {0}", clazz.getSimpleName());
            return null;
        }
        final EnchantmentBase enchant = ReflectionManager.instantiate(constructor, new NamespacedKey(addon, clazz.getSimpleName()));

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
