package com.flemmli97.fate.client.render;

import com.flemmli97.fate.common.blocks.tile.TileAltar;
import com.mojang.blaze3d.matrix.MatrixStack;
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

    public RenderAltar(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(TileAltar altar, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int uv) {
        ItemStack stack = altar.getCharm();
        if (!Minecraft.getInstance().isGamePaused())
            altar.clientTick();
        int ticker = altar.ticker();
        int summoningTick = altar.getSummoningTick();
        if (!stack.isEmpty()) {
            matrixStack.push();
            matrixStack.translate(0.5F, 1.125F, 0.5F);
            matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(ticker));
            matrixStack.translate(0, 0.06F * (float) Math.sin((ticker * Math.PI) / 180), 0);
            Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND, 15728880, OverlayTexture.DEFAULT_UV, matrixStack, buffer);
            matrixStack.pop();
        }
        NonNullList<ItemStack> catalyst = altar.getCatalyst();
        for (int i = 0; i < catalyst.size(); i++) {
            if (catalyst.get(i).isEmpty())
                continue;
            matrixStack.push();
            matrixStack.translate(0.5F, 1.125F, 0.5F);
            matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(ticker + (float) (i * 45.0)));
            matrixStack.translate(0, 0.01F * (float) Math.sin((ticker * Math.PI) / 180), 0);
            matrixStack.translate(3.5F - (summoningTick * 0.007), summoningTick * 0.007, 0);
            matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90));
            Minecraft.getInstance().getItemRenderer().renderItem(catalyst.get(i), ItemCameraTransforms.TransformType.GROUND, 15728880, OverlayTexture.DEFAULT_UV, matrixStack, buffer);
            matrixStack.pop();
        }
    }
}