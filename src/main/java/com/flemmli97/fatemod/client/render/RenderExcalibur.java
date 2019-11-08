package com.flemmli97.fatemod.client.render;

import org.apache.commons.lang3.tuple.Pair;

import com.flemmli97.fatemod.common.entity.EntityExcalibur;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.tenshilib.client.render.RenderBeam;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderExcalibur extends RenderBeam<EntityExcalibur>{
	
    private static final ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/excalibur.png");
    
    public RenderExcalibur(RenderManager renderManagerIn)
    {
        super(renderManagerIn, EntityExcalibur.radius);
    }  

	@Override
	public ResourceLocation getEntityTexture(EntityExcalibur entity) {
		return tex;
	}

	@Override
	public Pair<ResourceLocation, Float> startTexture(EntityExcalibur entity) {
		return null;
	}

	@Override
	public Pair<ResourceLocation, Float> endTexture(EntityExcalibur entity) {
		return null;
	}
	
	@Override
	public double segmentLength()
	{
		return 0;
	}

	@Override
	public int animationFrames(BeamPart part)
	{
		if(part==BeamPart.MIDDLE)
			return 1;
		return 1;
	}
}
