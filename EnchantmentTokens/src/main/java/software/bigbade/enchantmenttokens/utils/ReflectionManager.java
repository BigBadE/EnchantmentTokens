package software.bigbade.enchantmenttokens.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
