package io.github.flemmli97.fateubw.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.render.FateRenders;
import io.github.flemmli97.fateubw.common.blocks.entity.AltarBlockEntity;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

public class RenderAltar implements BlockEntityRenderer<AltarBlockEntity> {

    private final ResourceLocation texture = Fate.modRes("textures/misc/magic_circle_1.png");
    private final RenderUtils.BeamBuilder builder = new RenderUtils.BeamBuilder();
    private final ItemRenderer itemRenderer;

    public RenderAltar(BlockEntityRendererProvider.Context context) {
        this.builder.setEndColor(255, 255, 255, 0);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public boolean shouldRenderOffScreen(AltarBlockEntity blockEntity) {
        return true;
    }

    @Override
    public void render(AltarBlockEntity altar, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int light, int uv) {
        float ticker = altar.ticker() + partialTick;
        if (altar.isComplete()) {
            poseStack.pushPose();
            poseStack.translate(0.5F, 0.025F, 0.5F);
            poseStack.mulPose(Axis.YP.rotationDegrees(ticker * 0.5f));
            VertexConsumer vert = buffer.getBuffer(FateRenders.getFullBrightText(this.texture));
            Matrix4f mat = poseStack.last().pose();
            float size = 2.5f;
            vert.addVertex(mat, size, 0, size).setColor(255, 0, 0, 255).setUv(0, 0);
            vert.addVertex(mat, size, 0, -size).setColor(255, 0, 0, 255).setUv(0, 1);
            vert.addVertex(mat, -size, 0, -size).setColor(255, 0, 0, 255).setUv(1, 1);
            vert.addVertex(mat, -size, 0, size).setColor(255, 0, 0, 255).setUv(1, 0);

            vert.addVertex(mat, -size, 0, size).setColor(255, 0, 0, 255).setUv(1, 0);
            vert.addVertex(mat, -size, 0, -size).setColor(255, 0, 0, 255).setUv(1, 1);
            vert.addVertex(mat, size, 0, -size).setColor(255, 0, 0, 255).setUv(0, 1);
            vert.addVertex(mat, size, 0, size).setColor(255, 0, 0, 255).setUv(0, 0);
            poseStack.popPose();
        }
        ItemStack stack = altar.getCharm();
        if (!stack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5F, 1.125F, 0.5F);
            poseStack.mulPose(Axis.YP.rotationDegrees(ticker));
            poseStack.translate(0, 0.06F * (float) Math.sin((ticker * Math.PI) / 180), 0);
            this.itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, 0xf000f0, OverlayTexture.NO_OVERLAY, poseStack, buffer, altar.getLevel(), 0);
            poseStack.popPose();
        }
        NonNullList<ItemStack> catalyst = altar.getCatalyst();
        float summoningTick = altar.isSummoning() ? altar.getSummoningTick() + partialTick : 0;
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
                RenderUtils.renderGradientBeams3d(poseStack, buffer, 0.7f, 0.25f, altar.ticker(), partialTick, 5, 10, this.builder);
            poseStack.mulPose(Axis.YP.rotationDegrees(ticker * (altar.isSummoning() ? 2 : 1) + (float) (i * 45.0)));
            poseStack.translate(x, 0, 0);
            poseStack.mulPose(Axis.YP.rotationDegrees(90));
            this.itemRenderer.renderStatic(catalyst.get(i), ItemDisplayContext.GROUND, 0xf000f0, OverlayTexture.NO_OVERLAY, poseStack, buffer, altar.getLevel(), 0);
            poseStack.popPose();
        }
    }
}