package io.github.flemmli97.fateubw.mixin;

import com.mojang.blaze3d.vertex.VertexBuffer;
import io.github.flemmli97.fateubw.client.render.RenderUBWSky;
import io.github.flemmli97.fateubw.common.registry.FateDimensions;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Shadow
    @Final
    private Minecraft minecraft;
    @Shadow
    @Nullable
    private VertexBuffer skyBuffer;
    @Shadow
    @Nullable
    private VertexBuffer darkBuffer;
    @Shadow
    @Nullable
    private ClientLevel level;

    @Inject(method = "renderSky", at = @At("HEAD"), cancellable = true)
    private void onRenderSky(Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean isFoggy, Runnable skyFogSetup, CallbackInfo ci) {
        if (this.minecraft.level.dimension().equals(FateDimensions.UNLIMITED_BLADEWORKS.dimension())) {
            RenderUBWSky.renderUBWSky(this.level, this.skyBuffer, this.darkBuffer,
                    frustumMatrix, projectionMatrix, partialTick, skyFogSetup);
            ci.cancel();
        }
    }
}
