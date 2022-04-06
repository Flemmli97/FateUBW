package io.github.flemmli97.fateubw.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.flemmli97.fateubw.common.blocks.tile.AltarBlockEntity;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public class RenderAltar implements BlockEntityRenderer<AltarBlockEntity> {

    private final RenderUtils.BeamBuilder builder = new RenderUtils.BeamBuilder();

    public RenderAltar(BlockEntityRendererProvider.Context context) {
        this.builder.setEndColor(255, 255, 255, 0);
    }

    @Override
    public void render(AltarBlockEntity altar, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int light, int uv) {
        ItemStack stack = altar.getCharm();
        float ticker = altar.ticker() + partialTicks;
        float summoningTick = altar.getSummoningTick() + partialTicks;
        if (!stack.isEmpty()) {
            matrixStack.pushPose();
            matrixStack.translate(0.5F, 1.125F, 0.5F);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(ticker));
            matrixStack.translate(0, 0.06F * (float) Math.sin((ticker * Math.PI) / 180), 0);
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.GROUND, 0xf000f0, OverlayTexture.NO_OVERLAY, matrixStack, buffer, 0);
            matrixStack.popPose();
        }
        NonNullList<ItemStack> catalyst = altar.getCatalyst();
        double yTrans = 0.01F * (float) Math.sin((ticker * Math.PI) / 180) + summoningTick * 0.01;
        int centerTime = 140;
        double x = Math.max(2.5F - (summoningTick * 2.5 / centerTime), 0);
        for (int i = 0; i < catalyst.size(); i++) {
            if (catalyst.get(i).isEmpty())
                continue;
            matrixStack.pushPose();
            matrixStack.translate(0.5F, 1.125F, 0.5F);
            matrixStack.translate(0, yTrans, 0);
            if (summoningTick > centerTime - 15)
                RenderUtils.renderGradientBeams3d(matrixStack, buffer, 0.7f, 0.25f, altar.ticker(), partialTicks, 5, 10, this.builder);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(ticker * (altar.isSummoning() ? 2 : 1) + (float) (i * 45.0)));
            matrixStack.translate(x, 0, 0);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(90));
            Minecraft.getInstance().getItemRenderer().renderStatic(catalyst.get(i), ItemTransforms.TransformType.GROUND, 0xf000f0, OverlayTexture.NO_OVERLAY, matrixStack, buffer, 0);
            matrixStack.popPose();
        }
    }
}