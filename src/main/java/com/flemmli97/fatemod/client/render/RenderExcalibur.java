package com.flemmli97.fatemod.client.render;

import com.flemmli97.fatemod.common.entity.EntityExcalibur;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.tenshilib.client.render.RenderBeam;
import com.flemmli97.tenshilib.client.render.RenderUtils;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderExcalibur extends RenderBeam<EntityExcalibur>{
	
    private static final ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/excalibur.png");

    public RenderExcalibur(RenderManager renderManagerIn)
    {
        super(renderManagerIn, EntityExcalibur.radius, RenderUtils.defaultColor);
    }  

	@Override
	protected ResourceLocation getEntityTexture(EntityExcalibur entity) {
		return tex;
	}

}
