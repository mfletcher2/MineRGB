package me.mfletcher.minergb.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow
    @Nullable
    public LocalPlayer player;
    @Unique
    private int previousSelected = -1;

//    @Inject(method = "handleKeybinds", at = @At("TAIL"))
//    private void onHandleKeybinds(CallbackInfo ci) {
//        if (previousSelected != player.getInventory().selected) {
//            MineRGBClientController.setHotbarSlot(player.getInventory().selected);
//            previousSelected = player.getInventory().selected;
//            new Thread(MineRGBClientController::updateLeds);
//        }
//    }
}
