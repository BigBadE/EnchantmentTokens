/*
 * Custom enchantments for Minecraft
 * Copyright (C) 2021 Big_Bad_E
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

package com.bigbade.enchantmenttokens.utils;

import java.nio.ByteBuffer;

public final class ByteUtils {
    private static final ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);

    private ByteUtils() {
    }

    public static byte[] longToBytes(long x) {
        buffer.clear();
        buffer.putLong(0, x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        buffer.clear();
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    public static byte[] intToBytes(int x) {
        buffer.clear();
        buffer.putInt(0, x);
        return buffer.array();
    }

    public static int bytesToInt(byte[] bytes) {
        buffer.clear();
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip
        return buffer.getInt();
    }

    public static byte[] floatToBytes(float x) {
        buffer.clear();
        buffer.putFloat(0, x);
        return buffer.array();
    }

    public static float bytesToFloat(byte[] bytes) {
        buffer.clear();
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip
        return buffer.getFloat();
    }

    public static byte[] shortToBytes(short x) {
        buffer.clear();
        buffer.putShort(0, x);
        return buffer.array();
    }

    public static short bytesToShort(byte[] bytes) {
        buffer.clear();
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip
        return buffer.getShort();
    }

    public static byte[] doubleToBytes(float x) {
        buffer.clear();
        buffer.putDouble(0, x);
        return buffer.array();
    }

    public static double bytesToDouble(byte[] bytes) {
        buffer.clear();
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip
        return buffer.getDouble();
    }
}
