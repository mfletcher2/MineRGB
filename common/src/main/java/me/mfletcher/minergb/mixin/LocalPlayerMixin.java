package me.mfletcher.minergb.mixin;

import com.mojang.authlib.GameProfile;
import me.mfletcher.minergb.MineRGBClientController;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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


    @Override
    public void giveExperiencePoints(int xpPoints) {
        super.giveExperiencePoints(xpPoints);
        MineRGBClientController.setXp();
    }
}
