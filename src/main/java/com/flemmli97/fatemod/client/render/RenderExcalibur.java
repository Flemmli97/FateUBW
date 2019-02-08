package com.flemmli97.fatemod.client.render;

import org.apache.commons.lang3.tuple.Pair;

import com.flemmli97.fatemod.common.entity.EntityExcalibur;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.tenshilib.client.render.RenderBeam;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderExcalibur extends RenderBeam<EntityExcalibur>{
	
    private static final ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/excalibur.png");
    private static final Pair<ResourceLocation,Float> start = Pair.of(new ResourceLocation(LibReference.MODID, "textures/entity/excalibur_start.png"), 1f);
    private static final Pair<ResourceLocation,Float> end = Pair.of(new ResourceLocation(LibReference.MODID, "textures/entity/excalibur_end.png"), 0.5f);

    public RenderExcalibur(RenderManager renderManagerIn)
    {
        super(renderManagerIn, EntityExcalibur.radius);
        this.setAlpha(200);
    }  

	@Override
	public ResourceLocation getEntityTexture(EntityExcalibur entity) {
		return tex;
	}

	@Override
	public Pair<ResourceLocation, Float> startTexture(EntityExcalibur entity) {
		return start;
	}

	@Override
	public Pair<ResourceLocation, Float> endTexture(EntityExcalibur entity) {
		return end;
	}
	
	@Override
	public double segmentLength()
	{
		return 4;
	}

	@Override
	public int animationFrames(BeamPart part)
	{
		if(part==BeamPart.MIDDLE)
			return 8;
		return 1;
	}
}
