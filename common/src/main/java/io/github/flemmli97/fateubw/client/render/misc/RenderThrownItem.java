package io.github.flemmli97.fateubw.client.render.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.flemmli97.fateubw.client.particles.TrailRenderer;
import io.github.flemmli97.fateubw.client.render.FateRenders;
import io.github.flemmli97.fateubw.common.entity.misc.ThrownItemEntity;
import io.github.flemmli97.tenshilib.client.render.ItemProjectileRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RenderThrownItem extends ItemProjectileRenderer<ThrownItemEntity> {

    public RenderThrownItem(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ItemStack getRenderItemStack(ThrownItemEntity entity) {
        return entity.getWeapon();
    }

    @Override
    public void render(ThrownItemEntity entity, float rotation, float partialTick, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        this.scaleX = 2;
        this.scaleY = 2;
        this.scaleZ = 2;
        if (entity.getItemType() == ThrownItemEntity.ItemType.KANSHOU || entity.getItemType() ==  ThrownItemEntity.ItemType.BAKUYA) {
            stack.pushPose();
            stack.scale(this.scaleX, this.scaleY, this.scaleZ);
            stack.translate(0, 0.15, 0);
            float tick = (entity.tickCount + partialTick) * 100;
            if (entity.getItemType() ==  ThrownItemEntity.ItemType.KANSHOU) {
                tick *= -1;
            }
            stack.mulPose(Axis.YP.rotationDegrees(tick + 90 + Mth.lerp(partialTick, entity.yRotO, entity.getYRot())));
            stack.mulPose(Axis.XP.rotationDegrees(90));
            stack.mulPose(Axis.ZP.rotationDegrees(135 - Mth.lerp(partialTick, entity.xRotO, entity.getXRot())));
            stack.translate(0, -0.1, 0);
            Minecraft.getInstance().getItemRenderer().renderStatic(this.getRenderItemStack(entity), ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, stack, buffer, entity.level(), entity.getId());
            stack.popPose();
        } else {
            super.render(entity, rotation, partialTick, stack, buffer, packedLight);
        }
        if (entity.getItemType().info != null) {
            TrailRenderer.render(entity, entity.getItemType().info, entity.trailPositions(), buffer.getBuffer(FateRenders.TRAIL_TRANSLUCENT), partialTick);
        }
    }

    @Override
    public Type getRenderType(ThrownItemEntity entity) {
        return Type.WEAPON;
    }
}