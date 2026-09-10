package io.github.flemmli97.fateubw.client.render.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.flemmli97.fateubw.client.render.FateRenders;
import io.github.flemmli97.fateubw.client.render.vertex.AlphaVertexConsumerWrapper;
import io.github.flemmli97.fateubw.client.render.vertex.ClippingVertexConsumerWrapper;
import io.github.flemmli97.fateubw.common.entity.misc.WeaponProjectile;
import io.github.flemmli97.tenshilib.client.render.vertex.VertexUtils;
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
            float tick = entity.tickCount + entity.renderRand;
            tick = ((tick % 24000) + partialTick) / 24000.0f;
            VertexConsumer consumer = VertexUtils.SINGLE_FLOAT.create(buffer.getBuffer(FateRenders.BABYLON_RENDER), tick);
            consumer.addVertex(matrix4f, -size, -size, 0).setColor(color.x(), color.y(), color.z(), 1).setUv(0, 0);
            consumer.addVertex(matrix4f, size, -size, 0).setColor(color.x(), color.y(), color.z(), 1).setUv(1, 0);
            consumer.addVertex(matrix4f, size, size, 0).setColor(color.x(), color.y(), color.z(), 1).setUv(1, 1);
            consumer.addVertex(matrix4f, -size, size, 0).setColor(color.x(), color.y(), color.z(), 1).setUv(0, 1);

            consumer.addVertex(matrix4f, -size, size, 0).setColor(color.x(), color.y(), color.z(), 1).setUv(0, 1);
            consumer.addVertex(matrix4f, size, size, 0).setColor(color.x(), color.y(), color.z(), 1).setUv(1, 1);
            consumer.addVertex(matrix4f, size, -size, 0).setColor(color.x(), color.y(), color.z(), 1).setUv(1, 0);
            consumer.addVertex(matrix4f, -size, -size, 0).setColor(color.x(), color.y(), color.z(), 1).setUv(0, 0);
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
            VertexConsumer cons = clip == null ? buffer.getBuffer(renderType)
                    : ClippingVertexConsumerWrapper.wrap(buffer.getBuffer(FateRenders.getClippedRendertype(renderType)), clip, color, 0.1f);
            if (alpha != 1) {
                cons = new AlphaVertexConsumerWrapper(cons, alpha);
            }
            return cons;
        } : buffer;
        Minecraft.getInstance().getItemRenderer().renderStatic(this.getRenderItemStack(entity), ItemDisplayContext.GROUND, 0xff00ff, OverlayTexture.NO_OVERLAY, stack, buf, entity.level(), entity.getId());
        super.render(entity, rotation, partialTick, stack, buf, 0xff00ff);
        stack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(WeaponProjectile entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }

    public ItemStack getRenderItemStack(WeaponProjectile entity) {
        return entity.getWeapon();
    }

}