package software.bigbade.enchantmenttokens.utils;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class ReflectionManagerTest {
    private final boolean worked = false;

    public ReflectionManagerTest() {

    }

    @Test
    public void testReflectionManager() {
        ReflectionManagerTest test = (ReflectionManagerTest) ReflectionManager.instantiate(getClass());
        assertNotNull(test);
        Field field = ReflectionManager.getField(getClass(), "worked");
        assertEquals(false, ReflectionManager.getValue(field, test));
        ReflectionManager.removeFinalFromField(field, test);
        ReflectionManager.setValue(field, true, test);
        assertTrue((boolean) ReflectionManager.getValue(field, test));
    }
}
