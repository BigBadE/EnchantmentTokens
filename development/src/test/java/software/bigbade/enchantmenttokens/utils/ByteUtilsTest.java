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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ByteUtilsTest.class})
public class ByteUtilsTest {
    @Test
    public void testByteUtils() {
        Assert.assertEquals(5, ByteUtils.bytesToLong(ByteUtils.longToBytes(5)));
        Assert.assertEquals(5, ByteUtils.bytesToInt(ByteUtils.intToBytes(5)));
        Assert.assertEquals(5, ByteUtils.bytesToInt(ByteUtils.intToBytes(5)));
        Assert.assertEquals(5, ByteUtils.bytesToInt(ByteUtils.intToBytes(5)));
        Assert.assertEquals(5, ByteUtils.bytesToInt(ByteUtils.intToBytes(5)));
    }
}
