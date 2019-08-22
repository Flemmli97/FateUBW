package com.flemmli97.fatemod.client.render;

import com.flemmli97.fatemod.common.entity.EntityCasterCircle;

import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCasterCircle extends Render<EntityCasterCircle>{

	public RenderCasterCircle(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public boolean shouldRender(EntityCasterCircle livingEntity, ICamera camera, double camX, double camY,
			double camZ) {
		return false;
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCasterCircle entity) {
		return null;
	}

}
