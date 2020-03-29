package software.bigbade.enchantmenttokens.utils;

import software.bigbade.enchantmenttokens.EnchantmentTokens;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
        assert field != null;
        return field;
    }

    @Nullable
    public static Method getMethod(@Nonnull Class<?> clazz, @Nonnull String name) {
        Method method;
        try {
            method = clazz.getDeclaredMethod(name);
            method.setAccessible(true);
        } catch (NoSuchMethodException e) {
            return null;
        }
        return method;
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

    @Nullable
    public static Object instantiate(Constructor<?> constructor, Object... args) {
        try {
            System.out.println("Constructor: " + constructor + ", args: " + args[0]);
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not instantiate class " + constructor.getName(), e);
            return null;
        }
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
