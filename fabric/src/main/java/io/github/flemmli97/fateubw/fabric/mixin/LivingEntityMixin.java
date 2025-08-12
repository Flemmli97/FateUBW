package io.github.flemmli97.fateubw.fabric.mixin;

import io.github.flemmli97.fateubw.client.ClientCalls;
import io.github.flemmli97.fateubw.common.event.EventCalls;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickCall(CallbackInfo info) {
        EventCalls.tick((LivingEntity) (Object) this);
        if (((LivingEntity) (Object) this).level().isClientSide)
            ClientCalls.tick((LivingEntity) (Object) this);
    }

    @Inject(method = "hurt", at = @At(value = "HEAD"), cancellable = true)
    private void onAttacked(DamageSource damageSrc, float damageAmount, CallbackInfoReturnable<Boolean> info) {
        if (EventCalls.onHurt((LivingEntity) (Object) this, damageSrc, damageAmount))
            info.setReturnValue(false);
    }

    @ModifyVariable(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"), argsOnly = true)
    private float hurt(float origin, DamageSource source) {
        return EventCalls.damageCalculation((LivingEntity) (Object) this, source, origin);
    }

    @Inject(method = "heal", at = @At("HEAD"), cancellable = true)
    private void onHeal(float heal, CallbackInfo info) {
        if (!EventCalls.canHeal((LivingEntity) (Object) this))
            info.cancel();
    }
}
