package io.github.flemmli97.fateubw.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.platform.ClientPlatform;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;

public class RenderExcaliburItem extends BlockEntityWithoutLevelRenderer {

    private final RenderUtils.BeamBuilder beam = createBeam();

    public RenderExcaliburItem(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
        super(blockEntityRenderDispatcher, entityModelSet);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        render(stack, transformType, matrixStack, buffer, combinedLight, combinedOverlay, this.beam);
    }

    public static RenderUtils.BeamBuilder createBeam() {
        RenderUtils.BeamBuilder beam = new RenderUtils.BeamBuilder();
        beam.setStartColor(240, 240, 240, 210);
        beam.setEndColor(245, 245, 0, 0);
        return beam;
    }

    public static void render(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, RenderUtils.BeamBuilder beam) {
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        BakedModel model = renderer.getItemModelShaper().getItemModel(stack);
        if (model == null)
            model = renderer.getItemModelShaper().getModelManager().getMissingModel();
        RenderType rendertype = ItemBlockRenderTypes.getRenderType(stack, true);
        VertexConsumer ivertexbuilder = ItemRenderer.getFoilBufferDirect(buffer, rendertype, true, stack.hasFoil());
        ClientPlatform.INSTANCE.renderModelList(renderer, model, stack, combinedLight, combinedOverlay, matrixStack, ivertexbuilder);

        if (transformType != ItemTransforms.TransformType.GUI) {
            matrixStack.pushPose();
            matrixStack.translate(0.5, 0.5, 0.5);
            RenderUtils.renderGradientBeams3d(matrixStack, buffer, 1.5f, 0.5f, ClientHandler.clientTick, ClientHandler.getPartialTicks(), 90 / 200f, 20, beam);
            matrixStack.popPose();
        }
    }
}
