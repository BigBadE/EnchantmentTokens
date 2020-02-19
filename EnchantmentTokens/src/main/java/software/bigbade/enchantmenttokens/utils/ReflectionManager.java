package software.bigbade.enchantmenttokens.utils;

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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.logging.Level;

public class ReflectionManager {
    private ReflectionManager() {}

    public static void removeFinalFromField(@NotNull Field field, @Nullable Object instance) {
        field.setAccessible(true);
        Field modifiersField = getField(Field.class, "modifiers");

        modifiersField.setAccessible(true);
        setValue(modifiersField, field.getModifiers() & ~Modifier.FINAL, field);
    }

    @NotNull
    public static Field getField(@NotNull Class<?> clazz, @NotNull String name) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(name);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            EnchantLogger.log(Level.SEVERE, "Version changes with enchantments, please report this and the MC version");
        }
        assert field != null;
        return field;
    }

    public static Object getValue(Field field, Object instance) {
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            EnchantLogger.log(Level.SEVERE, "Could not get field reflexively");
        }
        return null;
    }

    public static void setValue(Field field, Object value, Object instance) {
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            EnchantLogger.log(Level.SEVERE, "Could not set field reflexively");
        }
    }

    public static Object instantiate(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            EnchantLogger.log(Level.SEVERE, "Could not instantiate class " + clazz.getSimpleName());
        }
        return null;
    }

    public static Object invoke(Method method, Object instance, Object... args) {
        try {
            return method.invoke(instance, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            EnchantLogger.log("Problem invoking method", e);
        }
        return null;
    }
}
