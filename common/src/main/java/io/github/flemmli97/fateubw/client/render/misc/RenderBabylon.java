package io.github.flemmli97.fateubw.client.render.misc;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.flemmli97.fateubw.client.render.FateRenders;
import io.github.flemmli97.fateubw.common.entity.misc.BabylonWeapon;
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

    private final Vector4f color = new Vector4f(1.0f, 0.85f, 0.3f, 0.7f);

    public RenderBabylon(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(BabylonWeapon projectile, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        if (projectile.idle) {
            stack.pushPose();
            float scale = Math.min(1, (projectile.tickCount + partialTicks) / 6f);
            stack.scale(scale, scale, scale);
            stack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, projectile.yRotO, projectile.getYRot())));
            stack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTicks, projectile.xRotO, projectile.getXRot())));
            stack.translate(0, 0, 0.25f);
            float size = 1.5f;
            Matrix4f matrix4f = stack.last().pose();
            VertexConsumer consumer = buffer.getBuffer(FateRenders.BABYLON_RENDER);
            float tick = projectile.tickCount + projectile.renderRand;
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
        }
        stack.pushPose();
        stack.scale(2, 2, 2);
        if (projectile.idle) {
            float yRot = Mth.lerp(partialTicks, projectile.yRotO, projectile.getYRot());
            float xRot = Mth.lerp(partialTicks, projectile.xRotO, projectile.getXRot());
            stack.mulPose(Axis.YP.rotationDegrees(yRot));
            stack.mulPose(Axis.XP.rotationDegrees(xRot));
            stack.translate(0, 0, Math.max(0, 2 * (0.8 - projectile.preparationState(partialTicks))));
            stack.mulPose(Axis.XP.rotationDegrees(-xRot));
            stack.mulPose(Axis.YP.rotationDegrees(-yRot));
        }
        // Item rendering sometimes use double vertexconsumer but clipped rendertype will always return default and thus crash
        // Use separate buffersource for that instead
        AtomicInteger state = new AtomicInteger();
        Vector4f clip;
        if (projectile.idle) {
            Vector3f normal = new Vector3f(0, 0, 1);
            Matrix3f matrix3f = new Matrix3f();
            matrix3f.identity();
            matrix3f.rotate(Axis.YP.rotationDegrees(180 + Mth.lerp(partialTicks, projectile.yRotO, projectile.getYRot())));
            matrix3f.rotate(Axis.XP.rotationDegrees(-Mth.lerp(partialTicks, projectile.xRotO, projectile.getXRot())));
            normal.mulTranspose(matrix3f);
            clip = FateRenders.createClippingPlane(normal, projectile, 0.25f);
        } else if (projectile.despawning()) {
            Vector3f normal = new Vector3f(0, 0, 1);
            Matrix3f matrix3f = new Matrix3f();
            matrix3f.identity();
            matrix3f.rotate(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, projectile.yRotO, projectile.getYRot())));
            matrix3f.rotate(Axis.XP.rotationDegrees(-Mth.lerp(partialTicks, projectile.xRotO, projectile.getXRot())));
            normal.mulTranspose(matrix3f);
            clip = FateRenders.createClippingPlane(normal, projectile, -projectile.despawnProgress() * 2f + 1f);
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
        if (projectile.idle) {
            stack.mulPose(Axis.YP.rotationDegrees(180));
        }
        stack.mulPose(Axis.YP.rotationDegrees(90 + Mth.lerp(partialTicks, projectile.yRotO, projectile.getYRot())));
        stack.mulPose(Axis.ZP.rotationDegrees(135 - Mth.lerp(partialTicks, projectile.xRotO, projectile.getXRot())));
        Minecraft.getInstance().getItemRenderer().renderStatic(this.getRenderItemStack(projectile), ItemDisplayContext.GROUND, 0xff00ff, OverlayTexture.NO_OVERLAY, stack, buf, projectile.level(), projectile.getId());
        super.render(projectile, rotation, partialTicks, stack, buf, 0xff00ff);
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