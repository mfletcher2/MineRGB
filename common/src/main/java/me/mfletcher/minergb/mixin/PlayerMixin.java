package me.mfletcher.minergb.mixin;

import me.mfletcher.minergb.MineRGBClientController;
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

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "hurt", at = @At("TAIL"))
    public void onHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
//        new Thread(MineRGBClientController::flashHurt).start();
        MineRGBClientController.setHurt();
//        MineRGBClientController.setHealth(getHealth(), getMaxHealth());
    }

    @Override
    public void heal(float healAmount) {
        super.heal(healAmount);
//        MineRGBClientController.setHealth(getHealth(), getMaxHealth());
    }

    @Inject(method = "giveExperiencePoints", at = @At("TAIL"))
    public void onGiveExperiencePoints(int xpPoints, CallbackInfo ci) {
        MineRGBClientController.setXp();
    }
}
