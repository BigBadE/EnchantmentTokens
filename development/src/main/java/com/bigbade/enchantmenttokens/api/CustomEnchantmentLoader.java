/*
 * Custom enchantments for Minecraft
 * Copyright (C) 2021 Big_Bad_E
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

package com.bigbade.enchantmenttokens.api;

import lombok.RequiredArgsConstructor;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.Plugin;
import com.bigbade.enchantmenttokens.EnchantmentTokens;
import com.bigbade.enchantmenttokens.configuration.ConfigurationManager;
import com.bigbade.enchantmenttokens.configuration.ConfigurationType;
import com.bigbade.enchantmenttokens.events.EnchantmentEvent;
import com.bigbade.enchantmenttokens.utils.ReflectionManager;
import com.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import com.bigbade.enchantmenttokens.utils.enchants.EnchantmentLoader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
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
            EnchantmentBase enchant = CustomEnchantmentLoader.loadClass(enchantClass, configuration, addon);
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
        EnchantmentBase enchant = CustomEnchantmentLoader.loadClass(clazz, plugin.getConfig(), plugin);
        if (enchant == null) {
            return;
        }
        enchant.loadConfig();
        for (Method method : clazz.getMethods()) {
            checkMethod(enchant, method);
        }
        main.getEnchantmentHandler().registerEnchant(enchant);

        plugin.saveConfig();
    }

    @Override
    public void loadAddon(EnchantmentAddon addon) {
        File file = new File(main.getDataFolder().getAbsolutePath() + FOLDER + addon.getName() + ".yml");
        FileConfiguration configuration = ConfigurationManager.loadConfigurationFile(file);

        for (Field field : addon.getClass().getDeclaredFields()) {
            ConfigurationManager.loadConfigForField(field, configuration, addon);
        }

        ConfigurationManager.saveConfiguration(file, configuration);
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
        if (CustomEnchantmentLoader.canEnchant(enchant, type)) {
            main.getListenerHandler().getListenerManager(type).add(event -> ReflectionManager.invoke(method, enchant, event), enchant.getEnchantment());
        }
    }

    private static boolean canEnchant(EnchantmentBase enchant, ListenerType type) {
        if (enchant.getTarget() == null) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "No target set for enchantment {0}", enchant.getEnchantmentName());
            return false;
        }
        return type.canTarget(enchant.getTarget());
    }

    @Nullable
    private static EnchantmentBase loadClass(Class<? extends EnchantmentBase> clazz, FileConfiguration configuration, Plugin addon) {
        assert configuration != null;
        ConfigurationSection section = ConfigurationManager.getSectionOrCreate(configuration, "enchants");

        EnchantmentBase enchant = ReflectionManager.instantiate(Objects.requireNonNull(ReflectionManager.getConstructor(clazz, NamespacedKey.class)), new NamespacedKey(addon, clazz.getSimpleName()));

        Objects.requireNonNull(enchant);

        ConfigurationSection enchantSection = ConfigurationManager.getSectionOrCreate(section, enchant.getEnchantmentName());

        CustomEnchantmentLoader.loadConfiguration(enchantSection, (Enchantment) enchant);

        boolean enabled = new ConfigurationType<>(true).getValue("enabled", enchantSection);

        return (enabled) ? enchant : null;
    }

    @SuppressWarnings("unchecked")
    private static void loadConfiguration(ConfigurationSection section, Enchantment base) {
        Class<? extends Enchantment> currentClass = base.getClass();
        while (currentClass != Enchantment.class) {
            for (Field field : currentClass.getDeclaredFields()) {
                ConfigurationManager.loadConfigForField(field, section, base);
            }
            currentClass = (Class<? extends Enchantment>) currentClass.getSuperclass();
        }
    }
}
