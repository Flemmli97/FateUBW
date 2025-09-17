package io.github.flemmli97.fateubw.fabric.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.flemmli97.fateubw.client.ShakeHandler;
import net.minecraft.client.Camera;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    private float partialTickTime;

    @Shadow
    private float xRot;

    @Shadow
    private float yRot;

    @WrapOperation(method = "setRotation", at = @At(value = "INVOKE", target = "Lorg/joml/Quaternionf;rotationYXZ(FFF)Lorg/joml/Quaternionf;", remap = false))
    private Quaternionf cameraInject(Quaternionf instance, float angleY, float angleX, float angleZ, Operation<Quaternionf> original) {
        float[] rotations = new float[]{angleY, angleX, angleZ};
        ShakeHandler.renderShaking((Camera) (Object) this, angleY, angleX, 0, this.partialTickTime,
                f -> rotations[0] += f * Mth.DEG_TO_RAD, f -> rotations[1] += f * Mth.DEG_TO_RAD, f -> rotations[2] += f * Mth.DEG_TO_RAD, false);
        this.xRot = rotations[1];
        this.yRot = rotations[0];
        return original.call(instance, rotations[0], rotations[1], rotations[2]);
    }
}
