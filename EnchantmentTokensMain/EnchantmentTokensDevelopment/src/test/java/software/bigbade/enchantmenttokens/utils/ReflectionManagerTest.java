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

import org.bukkit.Bukkit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ReflectionManager.class, Bukkit.class })
public class ReflectionManagerTest {
    @SuppressWarnings({"FieldMayBeFinal", "unused"})
    private boolean worked = false;

    @Test
    public void testReflectionManager() {
        mockStatic(Bukkit.class);
        when(Bukkit.getVersion()).thenReturn("1.15.2");
        ReflectionManagerTest test = ReflectionManager.instantiate(getClass());
        assertNotNull(test);
        Field field = ReflectionManager.getField(getClass(), "worked");
        assertNotNull(field);
        ReflectionManager.setValue(field, true, test);
        assertTrue((boolean) ReflectionManager.getValue(field, test));
    }
}
