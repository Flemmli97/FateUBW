package io.github.flemmli97.fateubw.client.render.misc;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.flemmli97.fateubw.client.render.FateRenders;
import io.github.flemmli97.fateubw.common.entity.misc.WeaponProjectile;
import io.github.flemmli97.tenshilib.client.VertexUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
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

public class RenderWeaponProjectile extends EntityRenderer<WeaponProjectile> {

    private static final MultiBufferSource.BufferSource SEP = MultiBufferSource.immediate(new ByteBufferBuilder(1536));

    public RenderWeaponProjectile(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(WeaponProjectile entity, float rotation, float partialTick, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        float yRot = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
        float xRot = -Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
        // Render babylon portal
        boolean babylonRender = entity.preparing() && entity.getWeaponType() == WeaponProjectile.Type.BABYLON;
        Vector4f color = entity.getWeaponType().mainColor;
        if (babylonRender) {
            stack.pushPose();
            float scale = Math.min(1, (entity.tickCount + partialTick) / 6f);
            stack.scale(scale, scale, scale);
            stack.mulPose(Axis.YP.rotationDegrees(yRot));
            stack.mulPose(Axis.XP.rotationDegrees(xRot));
            stack.translate(0, entity.getBbHeight() * 0.5, 0);
            float size = 1.5f;
            Matrix4f matrix4f = stack.last().pose();
            VertexConsumer consumer = buffer.getBuffer(FateRenders.BABYLON_RENDER);
            float tick = entity.tickCount + entity.renderRand;
            tick = ((tick % 24000) + partialTick) / 24000.0f;
            VertexUtils.addVertexData(
                    consumer.addVertex(matrix4f, -size, -size, 0).setColor(color.x(), color.y(), color.z(), 1).setUv(0, 0),
                    VertexUtils.SINGLE_FLOAT.get(),
                    tick
            );
            VertexUtils.addVertexData(
                    consumer.addVertex(matrix4f, size, -size, 0).setColor(color.x(), color.y(), color.z(), 1).setUv(1, 0),
                    VertexUtils.SINGLE_FLOAT.get(),
                    tick
            );
            VertexUtils.addVertexData(
                    consumer.addVertex(matrix4f, size, size, 0).setColor(color.x(), color.y(), color.z(), 1).setUv(1, 1),
                    VertexUtils.SINGLE_FLOAT.get(),
                    tick
            );
            VertexUtils.addVertexData(
                    consumer.addVertex(matrix4f, -size, size, 0).setColor(color.x(), color.y(), color.z(), 1).setUv(0, 1),
                    VertexUtils.SINGLE_FLOAT.get(),
                    tick
            );

            VertexUtils.addVertexData(
                    consumer.addVertex(matrix4f, -size, size, 0).setColor(color.x(), color.y(), color.z(), 1).setUv(0, 1),
                    VertexUtils.SINGLE_FLOAT.get(),
                    tick
            );
            VertexUtils.addVertexData(
                    consumer.addVertex(matrix4f, size, size, 0).setColor(color.x(), color.y(), color.z(), 1).setUv(1, 1),
                    VertexUtils.SINGLE_FLOAT.get(),
                    tick
            );
            VertexUtils.addVertexData(
                    consumer.addVertex(matrix4f, size, -size, 0).setColor(color.x(), color.y(), color.z(), 1).setUv(1, 0),
                    VertexUtils.SINGLE_FLOAT.get(),
                    tick
            );
            VertexUtils.addVertexData(
                    consumer.addVertex(matrix4f, -size, -size, 0).setColor(color.x(), color.y(), color.z(), 1).setUv(0, 0),
                    VertexUtils.SINGLE_FLOAT.get(),
                    tick
            );
            stack.popPose();
        }
        stack.pushPose();
        stack.scale(2, 2, 2);
        stack.translate(0, entity.getBbHeight() * 0.5, 0);
        stack.mulPose(Axis.YP.rotationDegrees(90 + yRot));
        stack.mulPose(Axis.ZP.rotationDegrees(xRot));
        if (babylonRender) {
            stack.translate(Math.max(0, 2 * (0.8 - entity.preparationState(partialTick))), 0, 0);
        }
        stack.translate(-entity.getBbWidth() * 0.25, 0, 0);
        stack.mulPose(Axis.ZP.rotationDegrees(135));
        // Item rendering sometimes use double vertexconsumer but clipped rendertype will always return default and thus crash
        // Use separate buffersource for that instead
        AtomicInteger state = new AtomicInteger();
        Vector4f clip;
        if (babylonRender) {
            Vector3f normal = new Vector3f(0, 0, 1);
            Matrix3f matrix3f = new Matrix3f();
            matrix3f.identity();
            matrix3f.rotate(Axis.YP.rotationDegrees(yRot));
            matrix3f.rotate(Axis.XP.rotationDegrees(xRot));
            normal.mul(matrix3f);
            clip = FateRenders.createClippingPlane(normal, entity, entity.getBbWidth() * 0.5f);
        } else if (entity.despawning()) {
            Vector3f normal = new Vector3f(0, 0, 1);
            Matrix3f matrix3f = new Matrix3f();
            matrix3f.identity();
            matrix3f.rotate(Axis.YP.rotationDegrees(yRot));
            matrix3f.rotate(Axis.XP.rotationDegrees(xRot));
            normal.mul(matrix3f);
            clip = FateRenders.createClippingPlane(normal, entity, -entity.despawnProgress() * 2f + 1f);
        } else {
            clip = null;
        }
        float alpha = entity.getWeaponType() == WeaponProjectile.Type.UBW ? entity.preparationState(partialTick) : 1;
        MultiBufferSource buf = clip != null || alpha != 1 ? renderType -> {
            boolean changed = false;
            if (clip != null) {
                renderType = FateRenders.getClippedRendertype(renderType, clip, color, 0.1f);
                changed = true;
            }
            int current = state.get();
            VertexConsumer cons = current == 1 && changed ? SEP.getBuffer(renderType) : buffer.getBuffer(renderType);
            if (alpha != 1) {
                cons = new AlphaVertexConsumerWrapper(cons, alpha);
            }
            if (changed) {
                if (current == 0 || current == 2)
                    state.set(1);
                else
                    state.set(2);
            }
            return cons;
        } : buffer;
        Minecraft.getInstance().getItemRenderer().renderStatic(this.getRenderItemStack(entity), ItemDisplayContext.GROUND, 0xff00ff, OverlayTexture.NO_OVERLAY, stack, buf, entity.level(), entity.getId());
        super.render(entity, rotation, partialTick, stack, buf, 0xff00ff);
        if (state.get() != 0) // other buffersource was used
            SEP.endBatch();
        stack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(WeaponProjectile entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }

    public ItemStack getRenderItemStack(WeaponProjectile entity) {
        return entity.getWeapon();
    }

    private static class AlphaVertexConsumerWrapper implements VertexConsumer {

        private final VertexConsumer parent;
        private final float alpha;

        private AlphaVertexConsumerWrapper(VertexConsumer parent, float alpha) {
            this.parent = parent;
            this.alpha = alpha;
        }

        @Override
        public VertexConsumer addVertex(float x, float y, float z) {
            return this.parent.addVertex(x, y, z);
        }

        @Override
        public VertexConsumer setColor(int r, int g, int b, int alpha) {
            return this.parent.setColor(r, g, b, (int) (alpha * this.alpha));
        }

        @Override
        public VertexConsumer setUv(float u, float v) {
            return this.parent.setUv(u, v);
        }

        @Override
        public VertexConsumer setUv1(int u, int v) {
            return this.parent.setUv1(u, v);
        }

        @Override
        public VertexConsumer setUv2(int u, int v) {
            return this.parent.setUv2(u, v);
        }

        @Override
        public VertexConsumer setNormal(float x, float y, float z) {
            return this.parent.setNormal(x, y, z);
        }
    }
}