package software.bigbade.enchantmenttokens.utils;

import org.junit.Test;

public class ByteUtilsTest {
    @Test
    public void testByteUtils() {
        ByteUtils utils = new ByteUtils();
        assert utils.bytesToLong(utils.longToBytes(5)) == 5;
    }
}
