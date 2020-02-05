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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.logging.Level;

public class ReflectionManager {
    public static void removeFinalFromField(Field field) {
        field.setAccessible(true);
        Field modifiersField = null;
        try {
            modifiersField = Field.class.getDeclaredField("modifiers");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        assert modifiersField != null;
        modifiersField.setAccessible(true);
        try {
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        modifiersField.setAccessible(true);
        try {
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Field getField(Class<?> clazz, String name) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(name);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            EnchantLogger.LOGGER.log(Level.SEVERE, "Version changes with enchantments, please report this and the MC version");
        }
        return field;
    }

    public static Object getValue(Field field, Object instance) {
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            EnchantLogger.LOGGER.log(Level.SEVERE, "Could not get field reflexively");
        }
        return null;
    }

    public static void setValue(Field field, Object value, Object instance) {
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            EnchantLogger.LOGGER.log(Level.SEVERE, "Could not set field reflexively");
        }
    }

    public static Object instantiate(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            EnchantLogger.LOGGER.log(Level.SEVERE, "Could not instantiate class " + clazz.getSimpleName());
        }
        return null;
    }

    public static Object invoke(Method method, Object instance, Object... args) {
        try {
            return method.invoke(instance, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
