package io.github.flemmli97.fate.client.render;

import com.flemmli97.tenshilib.client.render.RenderUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.flemmli97.fate.common.blocks.tile.TileAltar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.vector.Vector3f;

public class RenderAltar extends TileEntityRenderer<TileAltar> {

    private final RenderUtils.BeamBuilder builder = new RenderUtils.BeamBuilder();

    public RenderAltar(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
        this.builder.setEndColor(255, 255, 255, 0);
    }

    @Override
    public void render(TileAltar altar, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int uv) {
        ItemStack stack = altar.getCharm();
        float ticker = altar.ticker() + partialTicks;
        float summoningTick = altar.getSummoningTick() + partialTicks;
        if (!stack.isEmpty()) {
            matrixStack.push();
            matrixStack.translate(0.5F, 1.125F, 0.5F);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(ticker));
            matrixStack.translate(0, 0.06F * (float) Math.sin((ticker * Math.PI) / 180), 0);
            Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND, 0xf000f0, OverlayTexture.NO_OVERLAY, matrixStack, buffer);
            matrixStack.pop();
        }
        NonNullList<ItemStack> catalyst = altar.getCatalyst();
        double yTrans = 0.01F * (float) Math.sin((ticker * Math.PI) / 180) + summoningTick * 0.01;
        int centerTime = 140;
        double x = Math.max(2.5F - (summoningTick * 2.5 / centerTime), 0);
        for (int i = 0; i < catalyst.size(); i++) {
            if (catalyst.get(i).isEmpty())
                continue;
            matrixStack.push();
            matrixStack.translate(0.5F, 1.125F, 0.5F);
            matrixStack.translate(0, yTrans, 0);
            if (summoningTick > centerTime - 15)
                RenderUtils.renderGradientBeams3d(matrixStack, buffer, 0.7f, 0.25f, altar.ticker(), partialTicks, 5, 10, this.builder);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(ticker * (altar.isSummoning() ? 2 : 1) + (float) (i * 45.0)));
            matrixStack.translate(x, 0, 0);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(90));
            Minecraft.getInstance().getItemRenderer().renderItem(catalyst.get(i), ItemCameraTransforms.TransformType.GROUND, 0xf000f0, OverlayTexture.NO_OVERLAY, matrixStack, buffer);
            matrixStack.pop();
        }
    }
}