package me.mfletcher.minergb.mixin;

import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public class InventoryMixin {
    @Shadow public int selected;

    @Inject(method = "swapPaint", at = @At("TAIL"))
    public void onSwapPaint(double direction, CallbackInfo ci) {
//        MineRGBClientController.setHotbarSlot(selected);
//        new Thread(MineRGBClientController::updateLeds);
    }
}
