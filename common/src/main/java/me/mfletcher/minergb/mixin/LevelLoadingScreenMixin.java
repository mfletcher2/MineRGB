package me.mfletcher.minergb.mixin;

import me.mfletcher.minergb.MineRGBClientController;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.server.level.progress.StoringChunkProgressListener;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelLoadingScreen.class)
public class LevelLoadingScreenMixin {
    @Final
    @Shadow
    private StoringChunkProgressListener progressListener;

    @Inject(method = "render", at = @At("TAIL"))
    public void onRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        int progress = Mth.clamp(this.progressListener.getProgress(), 0, 100);
        new Thread(() -> MineRGBClientController.updateLoading(progress)).start();
    }
}
