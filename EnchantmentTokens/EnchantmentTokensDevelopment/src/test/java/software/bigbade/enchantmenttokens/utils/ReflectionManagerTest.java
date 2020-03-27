package software.bigbade.enchantmenttokens.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import software.bigbade.enchantmenttokens.commands.EnchantTabCompleter;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

@PrepareForTest({ ReflectionManager.class })
public class ReflectionManagerTest {
    private boolean worked = false;

    @Test
    public void testReflectionManager() {
        ReflectionManagerTest test = (ReflectionManagerTest) ReflectionManager.instantiate(getClass());
        assertNotNull(test);
        Field field = ReflectionManager.getField(getClass(), "worked");
        assertEquals(false, ReflectionManager.getValue(field, test));
        ReflectionManager.setValue(field, true, test);
        assertTrue((boolean) ReflectionManager.getValue(field, test));
    }
}
