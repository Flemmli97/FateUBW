package io.github.flemmli97.fate.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.client.ClientHandler;
import io.github.flemmli97.fate.client.model.ModelEA;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderEAItem extends ItemStackTileEntityRenderer {

    private final ModelEA model = new ModelEA();
    private final ResourceLocation base = new ResourceLocation(Fate.MODID, "textures/items/ea/ea_base.png");
    private final ResourceLocation blade = new ResourceLocation(Fate.MODID, "textures/items/ea/ea_blade.png");

    @Override
    public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        matrixStack.push();
        matrixStack.scale(1.0F, -1.0F, -1.0F);

        model.setBase(true);
        IVertexBuilder builder = ItemRenderer.getEntityGlintVertexBuilder(buffer, this.model.getRenderType(base), true, stack.hasEffect());
        model.render(matrixStack, builder, combinedLight, combinedOverlay, 1, 1, 1, 1);

        model.setBase(false);
        model.spinBlade(ClientHandler.clientTick, ClientHandler.getPartialTicks());
        IVertexBuilder builder2 = ItemRenderer.getEntityGlintVertexBuilder(buffer, this.model.getRenderType(blade), true, stack.hasEffect());
        model.render(matrixStack, builder2, combinedLight, combinedOverlay, 1, 1, 1, 1);
        matrixStack.pop();
    }

}
