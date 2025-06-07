package io.github.flemmli97.fateubw.fabric.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.flemmli97.fateubw.client.ShakeHandler;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Final
    @Shadow
    private Camera mainCamera;

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setup(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;ZZF)V", shift = At.Shift.AFTER))
    private void cameraInject(float partialTicks, long finishTimeNano, PoseStack stack, CallbackInfo info) {
        CameraAccessor acc = (CameraAccessor) this.mainCamera;
        ShakeHandler.renderShaking(this.mainCamera, this.mainCamera.getYRot(), this.mainCamera.getXRot(), 0, partialTicks,
                acc::setYRot, acc::setXRot, f -> stack.mulPose(Vector3f.ZP.rotationDegrees(f)));
    }
}
