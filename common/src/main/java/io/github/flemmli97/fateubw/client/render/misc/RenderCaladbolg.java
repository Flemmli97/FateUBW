package io.github.flemmli97.fateubw.client.render.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.misc.CaladBolg;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderCaladbolg extends EntityRenderer<CaladBolg> {

    public static final ResourceLocation TEX = Fate.modRes("textures/entity/caladbolg.png");

    public RenderCaladbolg(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(CaladBolg entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        matrixStack.pushPose();
        int size = 6;
        matrixStack.scale(0.2f, 0.2f, 0.2f);
        matrixStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
        matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
        matrixStack.translate(-size + 2, entity.getBbHeight() * 0.6f / 0.2f, -entity.getBbWidth() * 0.3f / 0.2f);
        matrixStack.mulPose(Axis.XP.rotationDegrees(45.0F));

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(entity)));
        PoseStack.Pose pose = matrixStack.last();
        float textureWidth = 64f / 64;
        float textureHeight = 11f / 64;
        float ratio = textureHeight / textureWidth;
        for (int r = 0; r < 4; ++r) {
            matrixStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            this.vertex(pose, vertexConsumer, -size, -size * ratio, 0, 0.0F, 0.0F, 0, 1, 0, packedLight);
            this.vertex(pose, vertexConsumer, size, -size * ratio, 0, textureWidth, 0.0F, 0, 1, 0, packedLight);
            this.vertex(pose, vertexConsumer, size, size * ratio, 0, textureWidth, textureHeight, 0, 1, 0, packedLight);
            this.vertex(pose, vertexConsumer, -size, size * ratio, 0, 0.0F, textureHeight, 0, 1, 0, packedLight);
        }

        matrixStack.popPose();
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer vertexBuilder, float x, float y, float z, float textureX, float textureY, float normalX, float normalY, float normalZ, int packedLight) {
        vertexBuilder.addVertex(pose, x, y, z).setColor(255, 255, 255, 255).setUv(textureX, textureY).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, normalX, normalZ, normalY);
    }

    @Override
    public ResourceLocation getTextureLocation(CaladBolg entity) {
        return TEX;
    }
}
