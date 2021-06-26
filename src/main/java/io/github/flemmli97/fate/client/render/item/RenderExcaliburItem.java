package io.github.flemmli97.fate.client.render.item;

import com.flemmli97.tenshilib.client.render.RenderUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.flemmli97.fate.client.ClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;

public class RenderExcaliburItem extends ItemStackTileEntityRenderer {

    private final RenderUtils.BeamBuilder beam = new RenderUtils.BeamBuilder();

    public RenderExcaliburItem() {
        this.beam.setStartColor(240, 240, 240, 210);
        this.beam.setEndColor(245, 245, 0, 0);
    }

    @Override
    public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        IBakedModel model = renderer.getItemModelMesher().getItemModel(stack);
        if (model == null)
            model = renderer.getItemModelMesher().getModelManager().getMissingModel();
        RenderType rendertype = RenderTypeLookup.func_239219_a_(stack, true);
        IVertexBuilder ivertexbuilder = ItemRenderer.getEntityGlintVertexBuilder(buffer, rendertype, true, stack.hasEffect());
        renderer.renderModel(model, stack, combinedLight, combinedOverlay, matrixStack, ivertexbuilder);

        if (transformType != ItemCameraTransforms.TransformType.GUI) {
            matrixStack.push();
            matrixStack.translate(0.5, 0.5, 0.5);
            RenderUtils.renderGradientBeams3d(matrixStack, buffer, 1.5f, 0.5f, ClientHandler.clientTick, ClientHandler.getPartialTicks(), 90 / 200f, 20, this.beam);
            matrixStack.pop();
        }
    }
}
