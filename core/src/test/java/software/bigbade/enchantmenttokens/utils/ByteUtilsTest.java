package software.bigbade.enchantmenttokens.utils;

import org.junit.Assert;
import org.junit.Test;

public class ByteUtilsTest {
    @Test
    public void testByteUtils() {
        ByteUtils utils = new ByteUtils();
        Assert.assertEquals(utils.bytesToLong(utils.longToBytes(5)), 5);
    }
}
