package me.mfletcher.minergb.neoforge;

import me.mfletcher.minergb.MineRGB;
import net.neoforged.fml.common.Mod;

@Mod(MineRGB.MOD_ID)
public final class MineRGBNeoForge {
    public MineRGBNeoForge() {
        // Run our common setup.
        MineRGB.init();
    }
}
