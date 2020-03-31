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

import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
