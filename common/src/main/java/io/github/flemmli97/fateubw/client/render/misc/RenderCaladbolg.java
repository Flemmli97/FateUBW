package io.github.flemmli97.fateubw.client.render.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
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

    public static final ResourceLocation TEX = new ResourceLocation(Fate.MODID, "textures/entity/caladbolg.png");

    public RenderCaladbolg(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(CaladBolg entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        matrixStack.pushPose();
        int size = 6;
        matrixStack.scale(0.2f, 0.2f, 0.2f);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
        matrixStack.translate(-size + 2, entity.getBbHeight() * 0.6f / 0.2f, -entity.getBbWidth() * 0.3f / 0.2f);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(45.0F));

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(entity)));
        PoseStack.Pose pose = matrixStack.last();
        Matrix4f matrix4f = pose.pose();
        Matrix3f matrix3f = pose.normal();
        float textureWidth = 64f / 64;
        float textureHeight = 11f / 64;
        float ratio = textureHeight / textureWidth;
        for (int r = 0; r < 4; ++r) {
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            this.vertex(matrix4f, matrix3f, vertexConsumer, -size, -size * ratio, 0, 0.0F, 0.0F, 0, 1, 0, packedLight);
            this.vertex(matrix4f, matrix3f, vertexConsumer, size, -size * ratio, 0, textureWidth, 0.0F, 0, 1, 0, packedLight);
            this.vertex(matrix4f, matrix3f, vertexConsumer, size, size * ratio, 0, textureWidth, textureHeight, 0, 1, 0, packedLight);
            this.vertex(matrix4f, matrix3f, vertexConsumer, -size, size * ratio, 0, 0.0F, textureHeight, 0, 1, 0, packedLight);
        }

        matrixStack.popPose();
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    public void vertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float x, float y, float z, float textureX, float textureY, float normalX, float normalY, float normalZ, int packedLight) {
        vertexBuilder.vertex(matrix, x, y, z).color(255, 255, 255, 255).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normals, normalX, normalZ, normalY).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(CaladBolg entity) {
        return TEX;
    }
}
