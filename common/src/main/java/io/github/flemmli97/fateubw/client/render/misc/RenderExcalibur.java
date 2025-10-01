package io.github.flemmli97.fateubw.client.render.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.misc.Excalibur;
import io.github.flemmli97.tenshilib.client.render.BeamRenderer;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.Random;

public class RenderExcalibur extends BeamRenderer<Excalibur> {

    public static final ResourceLocation TEX = Fate.modRes("textures/entity/excalibur.png");
    private static final Random RANDOM = new Random();

    private final float widthMod;

    public RenderExcalibur(EntityRendererProvider.Context ctx) {
        super(ctx, 1, 0.9f, 4);
        // For 4 sided polygon. Radius is diagonal so this is modifier to width
        this.widthMod = Mth.sqrt(0.9f * 0.9f / 2) * 2;
    }

    @Override
    public void render(Excalibur entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        matrixStack.pushPose();
        boolean playerView = entity.getOwner() == Minecraft.getInstance().player && Minecraft.getInstance().options.getCameraType() != CameraType.THIRD_PERSON_BACK;
        if (!playerView) {
            float y = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) + 90;
            float x = -Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
            matrixStack.mulPose(Axis.YN.rotationDegrees(y));
            matrixStack.mulPose(Axis.ZP.rotationDegrees(x));
            float scale = Mth.sin((entity.tickCount + partialTicks) * 1000) * 0.05f + 1f;
            matrixStack.scale(1, scale, scale);
            matrixStack.translate(0, RANDOM.nextFloat() * 0.2, RANDOM.nextFloat() * 0.2);
            matrixStack.mulPose(Axis.ZP.rotationDegrees(-x));
            matrixStack.mulPose(Axis.YN.rotationDegrees(-y));
        }
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
        matrixStack.popPose();
    }

    @Override
    public float widthFunc(Excalibur entity, float partialTicks) {
        return super.widthFunc(entity, partialTicks) / this.widthMod;
    }

    @Override
    public ResourceLocation getTextureLocation(Excalibur entity) {
        return TEX;
    }

    @Override
    public ResourcePair startTexture(Excalibur entity) {
        return null;
    }

    @Override
    public ResourcePair endTexture(Excalibur entity) {
        return null;
    }
}
