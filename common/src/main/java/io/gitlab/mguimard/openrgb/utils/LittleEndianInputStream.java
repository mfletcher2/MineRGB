package io.gitlab.mguimard.openrgb.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 * Utility class that ease the buffer reading process
 */
public class LittleEndianInputStream extends InputStream {
    private final InputStream in;

    public LittleEndianInputStream(InputStream in) {
        this.in = in;
    }

    @Override
    public int read() throws IOException {
        return in.read();
    }

    public int readInt() throws IOException {
        return byteBufferOfBytes(4).getInt();
    }

    public int readUnsignedShort() throws IOException {
        return byteBufferOfBytes(2).getShort() & 0xffff;
    }

    public String readAscii() throws IOException {
        int length = readUnsignedShort();
        byte[] bytes = new byte[length];
        in.read(bytes, 0, length);
        return new String(bytes, StandardCharsets.US_ASCII);
    }

    private ByteBuffer byteBufferOfBytes(int i) throws IOException {
        byte[] bytes = new byte[i];
        in.read(bytes, 0, i);
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
    }
}
