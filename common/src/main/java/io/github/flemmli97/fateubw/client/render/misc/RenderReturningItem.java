package io.github.flemmli97.fateubw.client.render.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.flemmli97.fateubw.common.entity.misc.ReturningItemProjectile;
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

public class RenderReturningItem extends EntityRenderer<ReturningItemProjectile> {

    public RenderReturningItem(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(ReturningItemProjectile entity, float rotation, float partialTick, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        float scale = 2;
        stack.pushPose();
        stack.scale(scale, scale, scale);
        stack.translate(0, 0.15, 0);
        float tick = (entity.tickCount + partialTick) * 100;
        if (entity.getItemType() == ReturningItemProjectile.ItemType.KANSHOU) {
            tick *= -1;
        }
        stack.mulPose(Axis.YP.rotationDegrees(tick + 90 + Mth.lerp(partialTick, entity.yRotO, entity.getYRot())));
        stack.mulPose(Axis.XP.rotationDegrees(90));
        stack.mulPose(Axis.ZP.rotationDegrees(135 - Mth.lerp(partialTick, entity.xRotO, entity.getXRot())));
        stack.translate(0, -0.1, 0);
        Minecraft.getInstance().getItemRenderer().renderStatic(this.getRenderItemStack(entity), ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, stack, buffer, entity.level(), entity.getId());
        stack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(ReturningItemProjectile entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }

    public ItemStack getRenderItemStack(ReturningItemProjectile entity) {
        return entity.getWeapon();
    }
}