package com.flemmli97.fatemod.client.render;

import com.flemmli97.fatemod.common.entity.EntityEnumaElish;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.tenshilib.client.render.RenderBeam;
import com.flemmli97.tenshilib.client.render.RenderUtils;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderEA extends RenderBeam<EntityEnumaElish>{
	
    private static final ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/ea.png");
    
    public RenderEA(RenderManager renderManagerIn)
    {
        super(renderManagerIn, EntityEnumaElish.radius, RenderUtils.defaultColor);
    }
    
	@Override
	protected ResourceLocation getEntityTexture(EntityEnumaElish entity) {
		return tex;
	}  
}
