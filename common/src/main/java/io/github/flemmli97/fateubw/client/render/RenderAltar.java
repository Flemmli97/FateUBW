package io.github.flemmli97.fateubw.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.blocks.entity.AltarBlockEntity;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class RenderAltar implements BlockEntityRenderer<AltarBlockEntity> {

    private final ResourceLocation texture = new ResourceLocation(Fate.MODID, "textures/misc/magic_circle.png");
    private final RenderUtils.BeamBuilder builder = new RenderUtils.BeamBuilder();

    public RenderAltar(BlockEntityRendererProvider.Context context) {
        this.builder.setEndColor(255, 255, 255, 0);
    }

    @Override
    public boolean shouldRenderOffScreen(AltarBlockEntity blockEntity) {
        return true;
    }

    @Override
    public void render(AltarBlockEntity altar, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light, int uv) {
        float ticker = altar.ticker() + partialTicks;
        if (altar.isComplete()) {
            poseStack.pushPose();
            poseStack.translate(0.5F, 0.025F, 0.5F);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(ticker * 0.5f));
            VertexConsumer vert = buffer.getBuffer(FateRenders.getPulsingEntityText(this.texture));
            Matrix4f mat = poseStack.last().pose();
            float size = 2.5f;
            vert.vertex(mat, size, 0, size).color(255, 255, 255, 255).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xff00ff).normal(1, 0, 0).endVertex();
            vert.vertex(mat, size, 0, -size).color(255, 255, 255, 255).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xff00ff).normal(1, 0, 0).endVertex();
            vert.vertex(mat, -size, 0, -size).color(255, 255, 255, 255).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xff00ff).normal(1, 0, 0).endVertex();
            vert.vertex(mat, -size, 0, size).color(255, 255, 255, 255).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xff00ff).normal(1, 0, 0).endVertex();

            vert.vertex(mat, -size, 0, size).color(255, 255, 255, 255).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xff00ff).normal(1, 0, 0).endVertex();
            vert.vertex(mat, -size, 0, -size).color(255, 255, 255, 255).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xff00ff).normal(1, 0, 0).endVertex();
            vert.vertex(mat, size, 0, -size).color(255, 255, 255, 255).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xff00ff).normal(1, 0, 0).endVertex();
            vert.vertex(mat, size, 0, size).color(255, 255, 255, 255).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xff00ff).normal(1, 0, 0).endVertex();
            poseStack.popPose();
        }
        ItemStack stack = altar.getCharm();
        if (!stack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5F, 1.125F, 0.5F);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(ticker));
            poseStack.translate(0, 0.06F * (float) Math.sin((ticker * Math.PI) / 180), 0);
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.GROUND, 0xf000f0, OverlayTexture.NO_OVERLAY, poseStack, buffer, 0);
            poseStack.popPose();
        }
        NonNullList<ItemStack> catalyst = altar.getCatalyst();
        float summoningTick = altar.isSummoning() ? altar.getSummoningTick() + partialTicks : 0;
        double yTrans = 0.01F * (float) Math.sin((ticker * Math.PI) / 180) + summoningTick * 0.01;
        int centerTime = 140;
        double x = Math.max(2.5F - (summoningTick * 2.5 / centerTime), 0);
        for (int i = 0; i < catalyst.size(); i++) {
            if (catalyst.get(i).isEmpty())
                continue;
            poseStack.pushPose();
            poseStack.translate(0.5F, 1.125F, 0.5F);
            poseStack.translate(0, yTrans, 0);
            if (summoningTick > centerTime - 15)
                RenderUtils.renderGradientBeams3d(poseStack, buffer, 0.7f, 0.25f, altar.ticker(), partialTicks, 5, 10, this.builder);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(ticker * (altar.isSummoning() ? 2 : 1) + (float) (i * 45.0)));
            poseStack.translate(x, 0, 0);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
            Minecraft.getInstance().getItemRenderer().renderStatic(catalyst.get(i), ItemTransforms.TransformType.GROUND, 0xf000f0, OverlayTexture.NO_OVERLAY, poseStack, buffer, 0);
            poseStack.popPose();
        }
    }
}