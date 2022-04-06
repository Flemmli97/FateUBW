package io.github.flemmli97.fateubw.client.render.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.misc.ChainDagger;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class RenderChainDagger extends EntityRenderer<ChainDagger> {

    private static final ResourceLocation tex = new ResourceLocation(Fate.MODID, "textures/entity/chain_dagger_tip.png");
    private static final ResourceLocation chain = new ResourceLocation(Fate.MODID, "textures/entity/chain.png");

    private static final RenderType RENDER_TYPE = RenderType.entityCutout(tex);
    private static final RenderType CHAIN_RENDER = RenderType.entityCutout(chain);

    public RenderChainDagger(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(ChainDagger entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        Entity owner = entity.getOwner();
        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
        if (entity.retracting())
            poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
        VertexConsumer vertexConsumer = buffer.getBuffer(RENDER_TYPE);
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix4f = pose.pose();
        Matrix3f matrix3f = pose.normal();

        poseStack.translate(0.05D, 0.15D, 0.0D);
        for (int r = 0; r < 4; ++r) {
            poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            this.vertex(matrix4f, matrix3f, vertexConsumer, -0.25f, -0.25f, 0, 0.0F, 0.0F, 0, 1, 0, packedLight);
            this.vertex(matrix4f, matrix3f, vertexConsumer, 0.25f, -0.25f, 0, 1, 0.0F, 0, 1, 0, packedLight);
            this.vertex(matrix4f, matrix3f, vertexConsumer, 0.25f, 0.25f, 0, 1, 1, 0, 1, 0, packedLight);
            this.vertex(matrix4f, matrix3f, vertexConsumer, -0.25f, 0.25f, 0, 0.0F, 1, 0, 1, 0, packedLight);
        }
        poseStack.popPose();
        poseStack.pushPose();
        if (owner instanceof LivingEntity living) {
            pose = poseStack.last();
            matrix4f = pose.pose();
            matrix3f = pose.normal();
            int i = entity.fromMainHand() ? 1 : -1;
            if (living.getMainHandItem().getItem() == ModItems.medusaDagger.get())
                i = 1;
            else if (living.getOffhandItem().getItem() == ModItems.medusaDagger.get())
                i = -1;
            float xOffset;
            float yOffset;
            float zOffset;
            if ((this.entityRenderDispatcher.options == null || this.entityRenderDispatcher.options.getCameraType().isFirstPerson()) && living == Minecraft.getInstance().player) {
                Vec3 vec3 = this.entityRenderDispatcher.camera.getNearPlane().getPointOnPlane((float) i * 0.8F, -0.2F);
                vec3 = vec3.scale(960.0D / this.entityRenderDispatcher.options.fov);
                float attackAnim = living.getAttackAnim(partialTicks);
                float g = Mth.sin(Mth.sqrt(attackAnim) * Mth.PI);
                vec3 = vec3.yRot(g * 0.5F);
                vec3 = vec3.xRot(-g * 0.5F);
                xOffset = (float) -vec3.x;
                yOffset = (float) -vec3.y - living.getEyeHeight();
                zOffset = (float) -vec3.z;
            } else {
                float yRot = Mth.lerp(partialTicks, living.yBodyRotO, living.yBodyRot) * Mth.DEG_TO_RAD;
                float hand = living.getBbWidth() * 0.6f;
                xOffset = Mth.cos(yRot) * i * hand;
                yOffset = (float) (-living.getEyeHeight() * 0.7);
                zOffset = Mth.sin(yRot) * i * hand;
            }

            double targetX = Mth.lerp(partialTicks, living.xo, living.getX()) - xOffset;
            double targetY = Mth.lerp(partialTicks, living.yo, living.getY()) - yOffset;
            double targetZ = Mth.lerp(partialTicks, living.zo, living.getZ()) - zOffset;

            Vec3 look = entity.getLookAngle().scale(entity.getBbWidth());
            double entityX = Mth.lerp(partialTicks, entity.xo, entity.getX()) - look.x;
            double entityY = Mth.lerp(partialTicks, entity.yo, entity.getY()) + entity.getBbHeight() * 0.5 - look.y;
            double entityZ = Mth.lerp(partialTicks, entity.zo, entity.getZ()) - look.z;

            float dX = (float) (targetX - entityX);
            float dY = (float) (targetY - entityY);
            float dZ = (float) (targetZ - entityZ);
            float lenHorizontal = Mth.sqrt(dX * dX + dZ * dZ);
            float pitch = Mth.wrapDegrees((float) (-(Mth.atan2(dY, lenHorizontal) * Mth.RAD_TO_DEG)));
            float yaw = -Mth.wrapDegrees((float) (Mth.atan2(dZ, dX) * Mth.RAD_TO_DEG) - 180);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(yaw));
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(pitch));
            poseStack.translate(-entity.getBbWidth() * 0.5, entity.getBbHeight() * 0.5, 0);

            float seg = Mth.sqrt(dX * dX + dY * dY + dZ * dZ) + living.getBbWidth() * 0.5f;
            int amount = Mth.ceil(seg);
            seg /= amount;

            vertexConsumer = buffer.getBuffer(CHAIN_RENDER);
            for (int w = 0; w <= amount - 2; ++w) {
                for (int r = 0; r < 4; ++r) {
                    poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
                    this.vertex(matrix4f, matrix3f, vertexConsumer, -0.4f - seg * (w + 1), -0.25f, 0, 0.0F, 0.0F, 0, 1, 0, packedLight);
                    this.vertex(matrix4f, matrix3f, vertexConsumer, 0.1f - seg * w, -0.25f, 0, 1, 0.0F, 0, 1, 0, packedLight);
                    this.vertex(matrix4f, matrix3f, vertexConsumer, 0.1f - seg * w, 0.25f, 0, 1, 1, 0, 1, 0, packedLight);
                    this.vertex(matrix4f, matrix3f, vertexConsumer, -0.4f - seg * (w + 1), 0.25f, 0, 0.0F, 1, 0, 1, 0, packedLight);
                }
            }
        }

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    public void vertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float x, float y, float z, float textureX, float textureY, float normalX, float normalY, float normalZ, int packedLight) {
        vertexBuilder.vertex(matrix, x, y, z).color(255, 255, 255, 255).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normals, normalX, normalZ, normalY).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(ChainDagger entity) {
        return tex;
    }
}
