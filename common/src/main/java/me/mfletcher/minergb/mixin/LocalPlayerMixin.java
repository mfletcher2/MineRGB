package me.mfletcher.minergb.mixin;

import com.mojang.authlib.GameProfile;
import me.mfletcher.minergb.MineRGBClientController;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {

    public LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @Inject(method = "hurt", at = @At("TAIL"))
    public void onHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        MineRGBClientController.setHurt();
    }

    @Override
    public void heal(float healAmount) {
        super.heal(healAmount);
    }

    @Inject(method = "setExperienceValues", at = @At("TAIL"))
    public void onSetExperienceValues(float currentXP, int maxXP, int level, CallbackInfo ci) {
        MineRGBClientController.setXp();
    }
}
