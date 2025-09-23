package io.github.flemmli97.fateubw.client.render.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.render.FateRenders;
import io.github.flemmli97.fateubw.common.entity.misc.EnkiduChains;
import io.github.flemmli97.tenshilib.client.VertexUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class RenderEnkiduChains extends EntityRenderer<EnkiduChains> {

    public static final ResourceLocation TEXTURE = Fate.modRes("textures/entity/enkidu_chain.png");

    private final Vector4f color = new Vector4f(234 / 255f, 165 / 255f, 37 / 255f, 0.7f);

    public RenderEnkiduChains(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(EnkiduChains entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        Vec3 start = entity.getStartPosition();
        float yRot = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
        float xRot = -Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        double x = Mth.lerp(partialTicks, entity.xo, entity.getX());
        double y = Mth.lerp(partialTicks, entity.yo, entity.getY());
        double z = Mth.lerp(partialTicks, entity.zo, entity.getZ());
        double dx = x - start.x();
        double dy = y - start.y();
        double dz = z - start.z();
        double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
        stack.pushPose();
        float scale = Math.min(1, (entity.tickCount + partialTicks) / 6f);
        stack.scale(scale, scale, scale);

        stack.mulPose(Axis.YP.rotationDegrees(yRot));
        stack.mulPose(Axis.XP.rotationDegrees(xRot));
        stack.translate(0, entity.getBbHeight() * 0.5, -len);
        stack.mulPose(Axis.XP.rotationDegrees(-xRot));
        stack.mulPose(Axis.YP.rotationDegrees(-yRot));

        stack.mulPose(Axis.YP.rotationDegrees(entity.getStartY()));
        stack.mulPose(Axis.XP.rotationDegrees(entity.getStartX()));
        float size = 1.5f;
        Matrix4f matrix4f = stack.last().pose();
        VertexConsumer consumer = buffer.getBuffer(FateRenders.BABYLON_RENDER);
        float tick = entity.tickCount + entity.renderRand;
        tick = ((tick % 24000) + partialTicks) / 24000.0f;
        VertexUtils.addVertexData(
                consumer.addVertex(matrix4f, -size, -size, 0).setColor(this.color.x(), this.color.y(), this.color.z(), 1).setUv(0, 0),
                VertexUtils.SINGLE_FLOAT.get(),
                tick
        );
        VertexUtils.addVertexData(
                consumer.addVertex(matrix4f, size, -size, 0).setColor(this.color.x(), this.color.y(), this.color.z(), 1).setUv(1, 0),
                VertexUtils.SINGLE_FLOAT.get(),
                tick
        );
        VertexUtils.addVertexData(
                consumer.addVertex(matrix4f, size, size, 0).setColor(this.color.x(), this.color.y(), this.color.z(), 1).setUv(1, 1),
                VertexUtils.SINGLE_FLOAT.get(),
                tick
        );
        VertexUtils.addVertexData(
                consumer.addVertex(matrix4f, -size, size, 0).setColor(this.color.x(), this.color.y(), this.color.z(), 1).setUv(0, 1),
                VertexUtils.SINGLE_FLOAT.get(),
                tick
        );
        VertexUtils.addVertexData(
                consumer.addVertex(matrix4f, -size, size, 0).setColor(this.color.x(), this.color.y(), this.color.z(), 1).setUv(0, 1),
                VertexUtils.SINGLE_FLOAT.get(),
                tick
        );

        VertexUtils.addVertexData(
                consumer.addVertex(matrix4f, size, size, 0).setColor(this.color.x(), this.color.y(), this.color.z(), 1).setUv(1, 1),
                VertexUtils.SINGLE_FLOAT.get(),
                tick
        );
        VertexUtils.addVertexData(
                consumer.addVertex(matrix4f, size, -size, 0).setColor(this.color.x(), this.color.y(), this.color.z(), 1).setUv(1, 0),
                VertexUtils.SINGLE_FLOAT.get(),
                tick
        );
        VertexUtils.addVertexData(
                consumer.addVertex(matrix4f, -size, -size, 0).setColor(this.color.x(), this.color.y(), this.color.z(), 1).setUv(0, 0),
                VertexUtils.SINGLE_FLOAT.get(),
                tick
        );
        stack.popPose();
        if (!entity.preparing()) {
            stack.pushPose();
            stack.translate(0, entity.getBbHeight() * 0.5, 0);
            stack.mulPose(Axis.YP.rotationDegrees(yRot + 90));
            stack.mulPose(Axis.ZP.rotationDegrees(xRot));
            if (entity.hasHooked()) {
                int mod = entity.tickCount % 10;
                if (mod < 3) {
                    stack.translate(0, entity.getRandom().nextDouble() * 0.05, entity.getRandom().nextDouble() * 0.05);
                }
            }
            consumer = buffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(entity)));
            this.renderChains(stack, consumer, (float) len, entity.getBbWidth() * 2);
            stack.popPose();
        }
    }

    protected void renderChains(PoseStack stack, VertexConsumer consumer, float length, float width) {
        for (int r = 0; r < 4; ++r) {
            stack.mulPose(Axis.XP.rotationDegrees(90.0F));
            PoseStack.Pose pose = stack.last();
            for (float sec = 0; sec < length; sec += width * 2) {
                float sectionNext = Math.min(sec + width * 2, length);
                float perc = sectionNext - sec / (width * 2);
                this.vertex(pose, consumer, sec, -width, 0, 0, 0, 1, 0, 0);
                this.vertex(pose, consumer, sectionNext, -width, 0, perc, 0, 1, 0, 0);
                this.vertex(pose, consumer, sectionNext, width, 0, perc, 1, 1, 0, 0);
                this.vertex(pose, consumer, sec, width, 0, 0, 1, 1, 0, 0);
            }
        }
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer vertexBuilder, float x, float y, float z, float textureX, float textureY, float normalX, float normalY, float normalZ) {
        vertexBuilder.addVertex(pose, x, y, z).setColor(255, 255, 255, 255).setUv(textureX, textureY).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xffffff).setNormal(pose, normalX, normalZ, normalY);
    }

    @Override
    public ResourceLocation getTextureLocation(EnkiduChains entity) {
        return TEXTURE;
    }
}