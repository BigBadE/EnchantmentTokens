/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.utils;

import java.nio.ByteBuffer;

public class ByteUtils {
    private final ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);

    public byte[] longToBytes(long x) {
        buffer.clear();
        buffer.putLong(0, x);
        return buffer.array();
    }

    public long bytesToLong(byte[] bytes) {
        buffer.clear();
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip
        return buffer.getLong();
    }
}