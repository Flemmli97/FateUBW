package com.flemmli97.fatemod.client.render;

import com.flemmli97.fatemod.common.blocks.tile.TileAltar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class RenderAltar extends TileEntitySpecialRenderer<TileAltar> {
	
	private int ticker;
	private int summoningTick;
	@Override
	public void renderTileEntityAt(TileAltar grail, double x, double y, double z, float partialTicks, int destroyStage) {
		ItemStack stack = grail.getCharm();
		if(!Minecraft.getMinecraft().isGamePaused())
			ticker++;
		if(!Minecraft.getMinecraft().isGamePaused())
			if(!grail.isSummoning())
				summoningTick=0;
			else
				summoningTick++;
		if(stack!=null)
		{
			GlStateManager.pushMatrix();
			GlStateManager.color(1F, 1F, 1F, 1F);
			GlStateManager.translate(x, y, z);	
			GlStateManager.translate(0.5F, 1.125F, 0.5F);
			GlStateManager.rotate(ticker, 0.0F, 1.0F, 0.0F);
			GlStateManager.disableLighting();
			GlStateManager.translate(0, 0.06F * (float) Math.sin((ticker*Math.PI)/180), 0);
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, TransformType.GROUND);
			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
		ItemStack[] catalyst = grail.getCatalyst();
		for(int i=0;i<catalyst.length;i++)
		{
			GlStateManager.pushMatrix();
			GlStateManager.color(1F, 1F, 1F, 1F);
			GlStateManager.translate(x, y, z);	
			GlStateManager.translate(0.5F, 1.125F, 0.5F);
			GlStateManager.rotate(ticker+(float)(i*45.0), 0.0F, 1.0F, 0.0F);
			GlStateManager.disableLighting();
			GlStateManager.translate(0, 0.01F * (float) Math.sin((ticker*Math.PI)/180), 0);
			GlStateManager.translate(3.5F-(summoningTick*0.007), 0+(summoningTick*0.007),0);
			Minecraft.getMinecraft().getRenderItem().renderItem(catalyst[i], TransformType.GROUND);
			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
		if(ticker>360)
		ticker=0;
	}

}
