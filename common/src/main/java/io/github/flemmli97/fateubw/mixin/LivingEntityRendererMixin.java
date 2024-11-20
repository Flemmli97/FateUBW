package io.github.flemmli97.fateubw.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.fateubw.common.entity.StandingVehicle;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {

    @Shadow
    protected M model;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isBaby()Z", ordinal = 0))
    private void nonSit(T entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight, CallbackInfo info) {
        if (StandingVehicle.stand(entity.getVehicle()))
            this.model.riding = false;
    }
}
