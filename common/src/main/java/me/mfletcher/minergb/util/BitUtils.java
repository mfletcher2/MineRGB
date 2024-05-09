package me.mfletcher.minergb.util;

import net.minecraft.core.Vec3i;

public class BitUtils {
    public static Vec3i getRGB(int color) {
        return new Vec3i((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF);
    }
}
