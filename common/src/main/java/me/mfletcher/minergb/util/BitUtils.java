package me.mfletcher.minergb.util;

import org.joml.Vector3i;

public class BitUtils {
    public static Vector3i getRGB(int color) {
        return new Vector3i((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF);
    }
}
