package me.mfletcher.minergb.forge;

import dev.architectury.platform.forge.EventBuses;
import me.mfletcher.minergb.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(me.mfletcher.minergb.MineRGB.MOD_ID)
public final class MineRGB {
    public MineRGB() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(me.mfletcher.minergb.MineRGB.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        me.mfletcher.minergb.MineRGB.init();

        ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () -> new ConfigGuiHandler.ConfigGuiFactory((client, parent) ->
                AutoConfig.getConfigScreen(ModConfig.class, parent).get()));
    }
}
