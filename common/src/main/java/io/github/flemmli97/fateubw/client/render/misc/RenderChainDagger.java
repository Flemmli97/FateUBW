package io.github.flemmli97.fateubw.client.render.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.misc.ChainDagger;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.utils.MathsHelper;
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
import org.joml.Vector3f;

public class RenderChainDagger extends EntityRenderer<ChainDagger> {

    public static final ResourceLocation TEX = Fate.modRes("textures/entity/chain_dagger_tip.png");
    public static final ResourceLocation CHAIN = Fate.modRes("textures/entity/chain.png");

    private static final RenderType RENDER_TYPE = RenderType.entityCutout(TEX);
    private static final RenderType CHAIN_RENDER = RenderType.entityCutout(CHAIN);

    public RenderChainDagger(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(ChainDagger entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        Entity owner = entity.getOwner();
        float yRot = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F;
        if (entity.retracting())
            yRot -= 180;
        float xRot = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        poseStack.pushPose();
        poseStack.translate(0, entity.getBbHeight() * 0.5, 0);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.ZP.rotationDegrees(xRot));
        VertexConsumer vertexConsumer = buffer.getBuffer(RENDER_TYPE);
        PoseStack.Pose pose = poseStack.last();
        for (int r = 0; r < 4; ++r) {
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            this.vertex(pose, vertexConsumer, -0.25f, -0.25f, 0, 0.0F, 0.0F, 1, 0, 0, packedLight);
            this.vertex(pose, vertexConsumer, 0.25f, -0.25f, 0, 1, 0.0F, 1, 0, 0, packedLight);
            this.vertex(pose, vertexConsumer, 0.25f, 0.25f, 0, 1, 1, 1, 0, 0, packedLight);
            this.vertex(pose, vertexConsumer, -0.25f, 0.25f, 0, 0.0F, 1, 1, 0, 0, packedLight);
        }
        poseStack.popPose();

        poseStack.pushPose();
        if (owner instanceof LivingEntity living) {
            int i = entity.fromMainHand() ? -1 : 1;
            if (i == -1 && living.getMainHandItem().isEmpty() && living.getOffhandItem().getItem() == FateItems.MEDUSA_DAGGER.get())
                i = 1;
            else if (living.getMainHandItem().getItem() == FateItems.MEDUSA_DAGGER.get())
                i = -1;
            double targetX;
            double targetY;
            double targetZ;

            if ((this.entityRenderDispatcher.options == null || this.entityRenderDispatcher.options.getCameraType().isFirstPerson()) && living == Minecraft.getInstance().cameraEntity) {
                Vector3f left = this.entityRenderDispatcher.camera.getLeftVector().mul((float) i * 0.2F, new Vector3f());
                Vector3f up = this.entityRenderDispatcher.camera.getUpVector().mul(-0.2F, new Vector3f());
                float attackAnim = living.getAttackAnim(partialTicks);
                float g = Mth.sin(Mth.sqrt(attackAnim) * Mth.PI);
                left = left.rotateY(g * 0.5F).rotateX(-g * 0.5F);
                up = up.rotateY(g * 0.5F).rotateX(-g * 0.5F);
                targetX = this.entityRenderDispatcher.camera.getPosition().x() + left.x() + up.x();
                targetY = this.entityRenderDispatcher.camera.getPosition().y() + left.y() + up.y();
                targetZ = this.entityRenderDispatcher.camera.getPosition().z() + left.z() + up.z();
            } else {
                float yRotLiving = Mth.lerp(partialTicks, living.yBodyRotO, living.yBodyRot) * Mth.DEG_TO_RAD;
                float hand = living.getBbWidth() * 0.6f;
                targetX = Mth.lerp(partialTicks, living.xo, living.getX()) + Mth.cos(yRotLiving) * i * hand;
                targetY = Mth.lerp(partialTicks, living.yo, living.getY()) + living.getEyeHeight() * 0.5;
                targetZ = Mth.lerp(partialTicks, living.zo, living.getZ()) + Mth.sin(yRotLiving) * i * hand;
            }

            Vec3 look = Vec3.directionFromRotation(xRot, -yRot + 90).scale(0.17);
            double entityX = Mth.lerp(partialTicks, entity.xo, entity.getX()) + look.x;
            double entityY = Mth.lerp(partialTicks, entity.yo, entity.getY()) + look.y;
            double entityZ = Mth.lerp(partialTicks, entity.zo, entity.getZ()) + look.z;

            float dX = (float) (targetX - entityX);
            float dY = (float) (targetY - entityY);
            float dZ = (float) (targetZ - entityZ);
            float[] yXRot = MathsHelper.YXRotFrom(dX, dY, dZ);
            poseStack.translate(look.x(), entity.getBbHeight() * 0.5 + look.y(), look.z());
            poseStack.mulPose(Axis.YP.rotationDegrees(-yXRot[0] - 90));
            poseStack.mulPose(Axis.ZP.rotationDegrees(-yXRot[1]));

            float len = Mth.sqrt(dX * dX + dY * dY + dZ * dZ) + living.getBbWidth() * 0;
            vertexConsumer = buffer.getBuffer(CHAIN_RENDER);
            this.renderChains(poseStack, vertexConsumer, packedLight, len, 0.3f);
        }

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    protected void renderChains(PoseStack stack, VertexConsumer consumer, int packedLight, float length, float width) {
        for (int r = 0; r < 4; ++r) {
            stack.mulPose(Axis.XP.rotationDegrees(90.0F));
            PoseStack.Pose pose = stack.last();
            for (float sec = 0; sec < length; sec += width * 2) {
                float sectionNext = Math.min(sec + width * 2, length);
                this.vertex(pose, consumer, sec, -width, 0, 0, 0, 1, 0, 0, packedLight);
                this.vertex(pose, consumer, sectionNext, -width, 0, 1, 0, 1, 0, 0, packedLight);
                this.vertex(pose, consumer, sectionNext, width, 0, 1, 15 / 16f, 1, 0, 0, packedLight);
                this.vertex(pose, consumer, sec, width, 0, 0, 15 / 16f, 1, 0, 0, packedLight);
            }
        }
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer vertexBuilder, float x, float y, float z, float textureX, float textureY, float normalX, float normalY, float normalZ, int packedLight) {
        vertexBuilder.addVertex(pose, x, y, z).setColor(255, 255, 255, 255).setUv(textureX, textureY).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, normalX, normalZ, normalY);
    }

    @Override
    public ResourceLocation getTextureLocation(ChainDagger entity) {
        return TEX;
    }
}
