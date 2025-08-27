package io.github.flemmli97.fateubw.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RenderExcaliburItem extends BlockEntityWithoutLevelRenderer {

    private final RenderUtils.BeamBuilder beam = createBeam();

    public RenderExcaliburItem(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
        super(blockEntityRenderDispatcher, entityModelSet);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        render(stack, transformType, matrixStack, buffer, combinedLight, combinedOverlay, this.beam);
    }

    public static RenderUtils.BeamBuilder createBeam() {
        RenderUtils.BeamBuilder beam = new RenderUtils.BeamBuilder();
        beam.setStartColor(240, 240, 240, 210);
        beam.setEndColor(245, 245, 0, 0);
        return beam;
    }

    public static void render(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, RenderUtils.BeamBuilder beam) {
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        BakedModel model = renderer.getItemModelShaper().getItemModel(stack);
        PoseStack.Pose last = poseStack.last();
        poseStack.popPose();
        renderer.render(stack, transformType, false, poseStack, buffer, combinedLight, combinedOverlay, model);
        poseStack.pushPose();
        PoseStack.Pose update = poseStack.last();
        update.pose().set(last.pose());
        update.normal().set(last.normal());

        if (transformType != ItemDisplayContext.GUI) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.5, 0.5);
            RenderUtils.renderGradientBeams3d(poseStack, buffer, 1.5f, 0.5f, ClientHandler.clientTick, ClientHandler.getPartialTicks(), 90 / 200f, 20, beam);
            poseStack.popPose();
        }
    }
}
