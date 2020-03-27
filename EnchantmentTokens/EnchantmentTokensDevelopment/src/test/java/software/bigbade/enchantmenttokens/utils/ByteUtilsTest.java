package software.bigbade.enchantmenttokens.utils;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest({ ByteUtilsTest.class })
public class ByteUtilsTest {
    @Test
    public void testByteUtils() {
        ByteUtils utils = new ByteUtils();
        Assert.assertEquals(5, utils.bytesToLong(utils.longToBytes(5)));
    }
}
