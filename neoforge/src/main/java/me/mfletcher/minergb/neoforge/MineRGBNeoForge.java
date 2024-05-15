package me.mfletcher.minergb.neoforge;

import me.mfletcher.minergb.MineRGB;
import me.mfletcher.minergb.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(MineRGB.MOD_ID)
public final class MineRGBNeoForge {
    public MineRGBNeoForge() {
        // Run our common setup.
        MineRGB.init();

        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (client, parent) -> AutoConfig.getConfigScreen(ModConfig.class, parent).get());
    }
}
