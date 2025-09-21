package io.github.flemmli97.fateubw.client.render.misc;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.flemmli97.fateubw.client.particles.TrailRenderer;
import io.github.flemmli97.fateubw.client.render.FateRenders;
import io.github.flemmli97.fateubw.common.entity.misc.BabylonWeapon;
import io.github.flemmli97.fateubw.common.particles.trail.TrailInfo;
import io.github.flemmli97.fateubw.common.particles.trail.provider.ParticlePositionProvider;
import io.github.flemmli97.tenshilib.client.VertexUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.concurrent.atomic.AtomicInteger;

public class RenderBabylon extends EntityRenderer<BabylonWeapon> {

    private static final MultiBufferSource.BufferSource SEP = MultiBufferSource.immediate(new ByteBufferBuilder(1536));

    private final Vector4f color = new Vector4f(255 / 255f, 216 / 255f, 76 / 255f, 0.7f);

    private final TrailInfo info = TrailInfo.builder(new ParticlePositionProvider.ParticlePositionData(0))
            .setColor(255 / 255f, 190 / 255f, 25 / 255f, 0.8f)
            .setColor2(255 / 255f, 205 / 255f, 100 / 255f, 0.6f)
            .setWidth(0.07f)
            .setWidth2(0.005f)
            .setInterpolation(1)
            .build();

    public RenderBabylon(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(BabylonWeapon entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        if (entity.preparing()) {
            stack.pushPose();
            float scale = Math.min(1, (entity.tickCount + partialTicks) / 6f);
            stack.scale(scale, scale, scale);
            stack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot())));
            stack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
            stack.translate(0, entity.getBbHeight() * 0.5, entity.getBbWidth() * 0.5);
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
        } else {
            TrailRenderer.render(entity, TrailInfo.builder(new ParticlePositionProvider.ParticlePositionData(0))
                    .setColor(255 / 255f, 217 / 255f, 100 / 255f, 0.7f)
                    .setColor2(255 / 255f, 217 / 255f, 67 / 255f, 0.3f)
                    .setWidth(0.07f)
                    .setWidth2(0.005f)
                    .setInterpolation(1)
                    .build(), entity.trailPositions(), buffer.getBuffer(FateRenders.TRAIL_TRANSLUCENT), partialTicks);
        }
        stack.pushPose();
        stack.scale(2, 2, 2);
        if (entity.preparing()) {
            float yRot = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
            float xRot = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
            stack.mulPose(Axis.YP.rotationDegrees(yRot));
            stack.mulPose(Axis.XP.rotationDegrees(xRot));
            stack.translate(0, 0, Math.max(0, 2 * (0.8 - entity.preparationState(partialTicks))));
            stack.mulPose(Axis.XP.rotationDegrees(-xRot));
            stack.mulPose(Axis.YP.rotationDegrees(-yRot));
            stack.mulPose(Axis.YP.rotationDegrees(180));
        }
        // Item rendering sometimes use double vertexconsumer but clipped rendertype will always return default and thus crash
        // Use separate buffersource for that instead
        AtomicInteger state = new AtomicInteger();
        Vector4f clip;
        if (entity.preparing()) {
            Vector3f normal = new Vector3f(0, 0, 1);
            Matrix3f matrix3f = new Matrix3f();
            matrix3f.identity();
            matrix3f.rotate(Axis.YP.rotationDegrees(180 + Mth.lerp(partialTicks, entity.yRotO, entity.getYRot())));
            matrix3f.rotate(Axis.XP.rotationDegrees(-Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
            normal.mul(matrix3f);
            clip = FateRenders.createClippingPlane(normal, entity, entity.getBbWidth() * 0.5f);
        } else if (entity.despawning()) {
            Vector3f normal = new Vector3f(0, 0, 1);
            Matrix3f matrix3f = new Matrix3f();
            matrix3f.identity();
            matrix3f.rotate(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot())));
            matrix3f.rotate(Axis.XP.rotationDegrees(-Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
            normal.mul(matrix3f);
            clip = FateRenders.createClippingPlane(normal, entity, -entity.despawnProgress() * 2f + 1f);
        } else {
            clip = null;
        }
        MultiBufferSource buf = clip != null ? renderType -> {
            RenderType rendertype = FateRenders.getClippedRendertype(renderType, clip, this.color, 0.1f);
            int current = state.get();
            VertexConsumer cons = current == 1 ? SEP.getBuffer(rendertype) : buffer.getBuffer(rendertype);
            if (current == 0 || current == 2)
                state.set(1);
            else
                state.set(2);
            return cons;
        } : buffer;
        stack.translate(0, 0.15f, 0);
        stack.mulPose(Axis.YP.rotationDegrees(90 + Mth.lerp(partialTicks, entity.yRotO, entity.getYRot())));
        stack.mulPose(Axis.ZP.rotationDegrees(135 - Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
        Minecraft.getInstance().getItemRenderer().renderStatic(this.getRenderItemStack(entity), ItemDisplayContext.GROUND, 0xff00ff, OverlayTexture.NO_OVERLAY, stack, buf, entity.level(), entity.getId());
        super.render(entity, rotation, partialTicks, stack, buf, 0xff00ff);
        if (state.get() != 0) // other buffersource was used
            SEP.endBatch();
        stack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(BabylonWeapon entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }

    public ItemStack getRenderItemStack(BabylonWeapon entity) {
        return entity.getWeapon();
    }
}