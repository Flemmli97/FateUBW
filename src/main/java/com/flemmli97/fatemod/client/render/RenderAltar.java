package com.flemmli97.fatemod.client.render;

import com.flemmli97.fatemod.common.blocks.tile.TileAltar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class RenderAltar extends TileEntitySpecialRenderer<TileAltar> {
	
	private int ticker;
	private int summoningTick;
	
	@Override
	public void render(TileAltar grail, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		ItemStack stack = grail.getCharm();
		if(!Minecraft.getMinecraft().isGamePaused())
		{
			this.ticker++;
			if(!grail.isSummoning())
				this.summoningTick=0;
			else
				this.summoningTick++;
		}
		if(stack!=null)
		{
			GlStateManager.pushMatrix();
			GlStateManager.color(1F, 1F, 1F, 1F);
			GlStateManager.translate(x, y, z);	
			GlStateManager.translate(0.5F, 1.125F, 0.5F);
			GlStateManager.rotate(this.ticker, 0.0F, 1.0F, 0.0F);
			GlStateManager.disableLighting();
			GlStateManager.translate(0, 0.06F * (float) Math.sin((this.ticker*Math.PI)/180), 0);
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, TransformType.GROUND);
			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
		NonNullList<ItemStack> catalyst = grail.getCatalyst();
		for(int i=0;i<catalyst.size();i++)
		{
			GlStateManager.pushMatrix();
			GlStateManager.color(1F, 1F, 1F, 1F);
			GlStateManager.translate(x, y, z);	
			GlStateManager.translate(0.5F, 1.125F, 0.5F);
			GlStateManager.rotate(this.ticker+(float)(i*45.0), 0.0F, 1.0F, 0.0F);
			GlStateManager.disableLighting();
			GlStateManager.translate(0, 0.01F * (float) Math.sin((this.ticker*Math.PI)/180), 0);
			GlStateManager.translate(3.5F-(this.summoningTick*0.007), 0+(this.summoningTick*0.007),0);
			GlStateManager.rotate(90, 0.0F, 1.0F, 0.0F);
			Minecraft.getMinecraft().getRenderItem().renderItem(catalyst.get(i), TransformType.GROUND);
			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
		if(this.ticker>360)
			this.ticker=0;
	}

}
