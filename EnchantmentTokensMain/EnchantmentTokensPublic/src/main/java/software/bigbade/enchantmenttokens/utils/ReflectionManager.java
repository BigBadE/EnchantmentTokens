/*
 * Addons for the Custom Enchantment API in Minecraft
 * Copyright (C) 2020 BigBadE
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

package software.bigbade.enchantmenttokens.utils;

import software.bigbade.enchantmenttokens.EnchantmentTokens;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.logging.Level;

public class ReflectionManager {
    private ReflectionManager() {}

    @Nonnull
    public static Field getField(@Nonnull Class<?> clazz, @Nonnull String name) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(name);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Version changes with enchantments, please report this and the MC version");
        }
        Objects.requireNonNull(field);
        return field;
    }

    @Nonnull
    public static Object getValue(@Nonnull Field field, @Nullable Object instance) {
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not get field reflexively");
        }
        throw new IllegalStateException("Could not get value for field " + field.getName() + " with instance " + instance);
    }

    public static void setValue(@Nonnull Field field, @Nullable Object value, @Nullable Object instance) {
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not set field reflexively");
        }
    }

    @Nonnull
    public static Object instantiate(@Nonnull Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not instantiate class " + clazz.getSimpleName());
        }
        throw new IllegalStateException("Could not instantiate class " + clazz.getSimpleName());
    }

    @Nonnull
    public static Object instantiate(@Nonnull Constructor<?> constructor, @Nullable Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Error instantiating constructor " + constructor.getName(), e);
        }
        throw new IllegalStateException("Could not instantiate constructor " + constructor.getName());
    }

    @Nullable
    public static Object invoke(@Nonnull Method method, @Nullable Object instance, @Nullable Object... args) {
        try {
            return method.invoke(instance, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Problem invoking method", e);
        }
        return null;
    }
}
