package io.github.flemmli97.fateubw.client.render.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.render.FateRenders;
import io.github.flemmli97.fateubw.common.entity.misc.AestusDomusBeam;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CommonColors;

public class RenderAestusDomusBeam extends EntityRenderer<AestusDomusBeam> {

    public static final ResourceLocation TEX = Fate.modRes("textures/entity/aestus_domus_beam.png");

    public RenderAestusDomusBeam(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(AestusDomusBeam entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        int tick = entity.tickCount - AestusDomusBeam.START_DELAY;
        if (tick < 0)
            return;
        poseStack.pushPose();
        poseStack.mulPose(Axis.YN.rotationDegrees(this.entityRenderDispatcher.camera.getYRot()));
        VertexConsumer consumer = buffer.getBuffer(FateRenders.getFullBrightText(this.getTextureLocation(entity)));
        PoseStack.Pose pose = poseStack.last();
        float size = 2.5f;
        float height = Math.min(11, (tick + partialTicks) * 4);
        int idx = tick % 3;
        float u = (idx * 32f) / 96;
        float u2 = ((idx + 1) * 32f) / 96;
        consumer.addVertex(pose, -size, height, 0).setColor(CommonColors.WHITE).setUv(u, 0.5f);
        consumer.addVertex(pose, size, height, 0).setColor(CommonColors.WHITE).setUv(u2, 0.5f);
        consumer.addVertex(pose, size, 0, 0).setColor(CommonColors.WHITE).setUv(u2, 1);
        consumer.addVertex(pose, -size, 0, 0).setColor(CommonColors.WHITE).setUv(u, 1);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(0, 0.01, 0);
        poseStack.mulPose(Axis.YP.rotationDegrees((entity.tickCount + partialTicks) * 15));
        pose = poseStack.last();
        consumer.addVertex(pose, -size, 0, -size).setColor(CommonColors.WHITE).setUv(0, 0);
        consumer.addVertex(pose, size, 0, -size).setColor(CommonColors.WHITE).setUv(32f / 96f, 0);
        consumer.addVertex(pose, size, 0, size).setColor(CommonColors.WHITE).setUv(32f / 96f, 0.5f);
        consumer.addVertex(pose, -size, 0, size).setColor(CommonColors.WHITE).setUv(0, 0.5f);

        consumer.addVertex(pose, -size, 0, size).setColor(CommonColors.WHITE).setUv(0, 0.5f);
        consumer.addVertex(pose, size, 0, size).setColor(CommonColors.WHITE).setUv(32f / 96f, 0.5f);
        consumer.addVertex(pose, size, 0, -size).setColor(CommonColors.WHITE).setUv(32f / 96f, 0);
        consumer.addVertex(pose, -size, 0, -size).setColor(CommonColors.WHITE).setUv(0, 0);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(AestusDomusBeam entity) {
        return TEX;
    }
}
