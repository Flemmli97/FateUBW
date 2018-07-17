package com.flemmli97.fatemod.client.render;

import com.flemmli97.fatemod.common.entity.EntityBabylonWeapon;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderBabylon extends RenderProjectileBase<EntityBabylonWeapon>{
	
	private static final ResourceLocation babylonIddle = new ResourceLocation(LibReference.MODID, "textures/entity/babylon.png");
	
    public RenderBabylon(RenderManager renderManagerIn)
    {
        super(renderManagerIn, true);
    }
    
	@Override
	public void doRender(EntityBabylonWeapon projectile, double x, double y, double z, float entityYaw,
			float partialTick) {
		if(projectile.iddle)
		{
			this.bindTexture(babylonIddle);
	        GlStateManager.pushMatrix();
	        GlStateManager.disableCull();
	        RenderHelper.disableStandardItemLighting();
	        GlStateManager.translate((float)x, (float)y+0.2, (float)z);
	        GlStateManager.rotate(projectile.prevRotationYaw + (projectile.rotationYaw - projectile.prevRotationYaw) * partialTick - 90.0F, 0.0F, 1.0F, 0.0F);
	        GlStateManager.rotate(projectile.prevRotationPitch + (projectile.rotationPitch - projectile.prevRotationPitch) * partialTick -90, 0.0F, 0.0F, 1.0F);
	        Tessellator tessellator = Tessellator.getInstance();
	        BufferBuilder vertexbuffer = tessellator.getBuffer();	
	        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
	        vertexbuffer.pos(-0.8D, 0D, -0.8D).tex((double)0, (double)0).endVertex();
	        vertexbuffer.pos(-0.8D, 0D, 0.8D).tex((double)0, (double)1).endVertex();
	        vertexbuffer.pos(0.8D, 0D, 0.8D).tex((double)1, (double)1).endVertex();
	        vertexbuffer.pos(0.8D, 0D, -0.8D).tex((double)1, (double)0).endVertex();
	        tessellator.draw();	
	        RenderHelper.enableStandardItemLighting();
	        GlStateManager.enableCull();
	        GlStateManager.popMatrix();
		}
		super.doRender(projectile, x, y, z, entityYaw, partialTick);
	}

	@Override
	public ItemStack renderItemStackFromEntity(EntityBabylonWeapon entity) {
		return entity.getWeapon();
	}

	@Override
	public int getItemType() {
		return 0;
	}
    	
}
