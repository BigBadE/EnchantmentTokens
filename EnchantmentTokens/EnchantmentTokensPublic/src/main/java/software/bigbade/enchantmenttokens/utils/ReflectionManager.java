package software.bigbade.enchantmenttokens.utils;

import org.jetbrains.annotations.NotNull;
import software.bigbade.enchantmenttokens.EnchantmentTokens;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

public class ReflectionManager {
    private ReflectionManager() {}

    @NotNull
    public static Field getField(@NotNull Class<?> clazz, @NotNull String name) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(name);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Version changes with enchantments, please report this and the MC version");
        }
        assert field != null;
        return field;
    }

    public static Object getValue(Field field, Object instance) {
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not get field reflexively");
        }
        return null;
    }

    public static void setValue(Field field, Object value, Object instance) {
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not set field reflexively");
        }
    }

    public static Object instantiate(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not instantiate class " + clazz.getSimpleName());
        }
        return null;
    }

    public static Object instantiate(Constructor<?> constructor, Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            EnchantLogger.log(Level.SEVERE, "Could not instantiate class " + constructor.getName());
        }
        return null;
    }

    public static Object invoke(Method method, Object instance, Object... args) {
        try {
            return method.invoke(instance, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Problem invoking method", e);
        }
        return null;
    }
}
