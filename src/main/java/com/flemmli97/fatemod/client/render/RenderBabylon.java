package com.flemmli97.fatemod.client.render;

import com.flemmli97.fatemod.common.entity.EntityBabylonWeapon;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.tenshilib.client.render.RenderProjectileItem;
import com.flemmli97.tenshilib.client.render.RenderUtils;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderBabylon extends RenderProjectileItem<EntityBabylonWeapon>{
	
	private static final ResourceLocation babylonIddle = new ResourceLocation(LibReference.MODID, "textures/entity/babylon.png");
	
    public RenderBabylon(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }
    
	@Override
	public void doRender(EntityBabylonWeapon projectile, double x, double y, double z, float entityYaw, float partialTick) {
		if(projectile.iddle)
		{
			GlStateManager.pushMatrix();
			double ripple = Math.sin((projectile.ticksExisted+projectile.renderRand)/2f)*0.025+1;
			float size = (float) (1.6*ripple);
			RenderUtils.renderTexture(this.renderManager, babylonIddle, x, y, z, size, size, RenderUtils.defaultColor,
					projectile.prevRotationYaw + (projectile.rotationYaw - projectile.prevRotationYaw) * partialTick+180, 
					projectile.prevRotationPitch + (projectile.rotationPitch - projectile.prevRotationPitch) * partialTick+5);
			GlStateManager.popMatrix();
		}
		super.doRender(projectile, x, y, z, entityYaw, partialTick);
	}
	
	@Override
	public ItemStack getRenderItemStack(EntityBabylonWeapon entity) {
		return entity.getWeapon();
	}

	@Override
	public RenderType getRenderType(EntityBabylonWeapon entity) {
		return RenderType.WEAPON;
	}
    	
}
