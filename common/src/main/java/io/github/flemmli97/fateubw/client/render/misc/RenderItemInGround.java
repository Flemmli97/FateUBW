package io.github.flemmli97.fateubw.client.render.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.flemmli97.fateubw.common.entity.misc.ItemInGroundEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;

public class RenderItemInGround extends EntityRenderer<ItemInGroundEntity> {

    public RenderItemInGround(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(ItemInGroundEntity entity, float rotation, float partialTick, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.translate(-entity.getBbWidth() * 0.5, 0.7, 0);
        stack.scale(3.5f, 3.5f, 3.5f);
        stack.mulPose(Axis.ZP.rotationDegrees(225));
        Minecraft.getInstance().getItemRenderer().renderStatic(entity.getItem(), ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, stack, buffer, entity.level(), entity.getId());
        stack.popPose();
        super.render(entity, rotation, partialTick, stack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ItemInGroundEntity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
