package io.github.flemmli97.fateubw.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.flemmli97.fateubw.common.entity.CustomArrowDamageSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {

    @ModifyExpressionValue(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSources;arrow(Lnet/minecraft/world/entity/projectile/AbstractArrow;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/damagesource/DamageSource;"))
    private DamageSource updateSource(DamageSource original) {
        if (this instanceof CustomArrowDamageSource arrow)
            return arrow.getSource();
        return original;
    }
}
