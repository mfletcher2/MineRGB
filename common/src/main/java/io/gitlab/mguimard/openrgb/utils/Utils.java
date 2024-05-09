package io.gitlab.mguimard.openrgb.utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Miscellaneous utilities
 */
public class Utils {

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    private Utils() {
    }

    /**
     * Transforms a byte array in an almost readable string
     *
     * @param bytes the bytes to print
     * @return the string representing the bytes
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }

        String val = "2";
        return new String(hexChars).replaceAll("(.{" + val + "})", "$0 ").trim();
    }

    /**
     * Creates a byte array with a int inside
     *
     * @param value the int value
     * @return the byte array
     */
    public static byte[] byteArrayFromInt(int value) {
        return ByteBuffer
                .allocate(4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(value)
                .array();
    }

    /**
     * Fully read an input stream and returns the bytes
     *
     * @param is     the input stream
     * @param length the length to read
     * @return the byte array
     * @throws IOException
     */
    public static byte[] readFully(InputStream is, int length) throws IOException {
        byte[] data = new byte[length];
        DataInputStream dis = new DataInputStream(is);
        dis.readFully(data);
        return data;
    }

}
