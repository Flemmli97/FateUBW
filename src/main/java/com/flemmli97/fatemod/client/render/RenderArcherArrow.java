package com.flemmli97.fatemod.client.render;

import com.flemmli97.fatemod.common.entity.EntityArcherArrow;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderArcherArrow extends RenderArrow<EntityArcherArrow>{
	
    private static final ResourceLocation DEFAULT_RES_LOC = new ResourceLocation(LibReference.MODID, "textures/entity/arrows.png");

    public RenderArcherArrow(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityArcherArrow entity) {
		return DEFAULT_RES_LOC;
	}
	
	@Override
	public void doRender(EntityArcherArrow projectile, double x, double y, double z, float entityYaw,
			float partialTick) {
		this.bindEntityTexture(projectile);
        super.doRender(projectile, x, y, z, entityYaw, partialTick);
	}
}
