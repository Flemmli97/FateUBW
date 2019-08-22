package com.flemmli97.fatemod.client.render;

import org.apache.commons.lang3.tuple.Pair;

import com.flemmli97.fatemod.common.entity.EntityEnumaElish;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.tenshilib.client.render.RenderBeam;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderEA extends RenderBeam<EntityEnumaElish>{
	
    private static final ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/ea.png");
    
    public RenderEA(RenderManager renderManagerIn)
    {
        super(renderManagerIn, EntityEnumaElish.radius);
    }
    
	@Override
	protected ResourceLocation getEntityTexture(EntityEnumaElish entity) {
		return tex;
	}

	@Override
	public Pair<ResourceLocation, Float> startTexture(EntityEnumaElish entity) {
		return null;
	}

	@Override
	public Pair<ResourceLocation, Float> endTexture(EntityEnumaElish entity) {
		return null;
	}  
}
